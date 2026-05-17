import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Save, Loader, Plus, Trash2, Image as ImageIcon, UploadCloud, Smartphone, DollarSign, Box } from 'lucide-react';
import { adminCreateProduct, adminUploadFile } from '../../services/adminApi';

interface Variant {
  sku: string;
  color: string;
  storage: string;
  originalPrice: number;
  discountedPrice: number;
  stockQuantity: number;
  imageURL: string;
}

export const AdminProductAdd = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    category: '',
    brand: '',
    imageURL: '',
    specs: '',
  });

  const [variants, setVariants] = useState<Variant[]>([
    { sku: '', color: '', storage: '', originalPrice: 0, discountedPrice: 0, stockQuantity: 0, imageURL: '' }
  ]);

  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState<string | null>(null); // Để track đang upload ảnh nào
  const [error, setError] = useState('');

  // Helper: Tạo SKU tự động
  const generateSKU = (name: string, color: string, storage: string) => {
    const clean = (str: string) => str.trim().toUpperCase().replace(/\s+/g, '-');
    const parts = [clean(name || 'PROD')];
    if (color) parts.push(clean(color));
    if (storage) parts.push(clean(storage));
    return parts.join('-');
  };

  const handleVariantChange = (index: number, field: keyof Variant, value: any) => {
    const newVariants = [...variants];
    newVariants[index] = { ...newVariants[index], [field]: value };
    
    // Nếu đổi color/storage mà SKU trống hoặc đang khớp với pattern cũ, thì gợi ý SKU mới
    if (field === 'color' || field === 'storage') {
      newVariants[index].sku = generateSKU(formData.name, newVariants[index].color, newVariants[index].storage);
    }
    
    setVariants(newVariants);
  };

  const addVariant = () => {
    setVariants([...variants, { sku: '', color: '', storage: '', originalPrice: 0, discountedPrice: 0, stockQuantity: 0, imageURL: '' }]);
  };

  const removeVariant = (index: number) => {
    if (variants.length > 1) {
      setVariants(variants.filter((_, i) => i !== index));
    }
  };

  const handleFileUpload = async (file: File, type: 'main' | 'variant', index?: number) => {
    const uploadKey = type === 'main' ? 'main' : `variant-${index}`;
    setUploading(uploadKey);
    try {
      const res = await adminUploadFile(file);
      if (type === 'main') {
        setFormData({ ...formData, imageURL: res.url });
      } else if (index !== undefined) {
        const newVariants = [...variants];
        newVariants[index].imageURL = res.url;
        setVariants(newVariants);
      }
    } catch (e: any) {
      alert('Upload thất bại: ' + e.message);
    } finally {
      setUploading(null);
    }
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      // Group variants by SKU and Storage to avoid model mismatch
      const groupedMap = new Map<string, any>();
      
      variants.forEach(v => {
        const key = `${v.sku}-${v.storage}`;
        if (!groupedMap.has(key)) {
          groupedMap.set(key, {
            sku: v.sku,
            storage: v.storage,
            originalPrice: isNaN(Number(v.originalPrice)) ? 0 : Number(v.originalPrice),
            discountedPrice: isNaN(Number(v.discountedPrice)) ? 0 : Number(v.discountedPrice),
            stockQuantity: 0,
            variantColors: []
          });
        }
        
        const group = groupedMap.get(key);
        group.variantColors.push({
          color: v.color || 'Default',
          stockQuantity: isNaN(Number(v.stockQuantity)) ? 0 : Number(v.stockQuantity),
          imageUrl: v.imageURL
        });
        group.stockQuantity += isNaN(Number(v.stockQuantity)) ? 0 : Number(v.stockQuantity);
      });

      await adminCreateProduct({
        ...formData,
        category: { name: formData.category },
        brand: { name: formData.brand },
        variants: Array.from(groupedMap.values())
      });
      navigate('/admin/products');
    } catch (e: any) {
      setError('Thêm thất bại: ' + e.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="max-w-5xl mx-auto pb-20">
      <div className="flex items-center gap-4 mb-6">
        <button onClick={() => navigate('/admin/products')} className="p-2 hover:bg-white rounded-full transition-colors text-gray-500">
          <ArrowLeft size={24} />
        </button>
        <div>
          <h1 className="text-2xl font-black text-gray-800 uppercase italic">Thêm sản phẩm & Biến thể</h1>
          <p className="text-sm text-gray-500 font-medium">Tạo sản phẩm mới với nhiều tùy chọn màu sắc, cấu hình</p>
        </div>
      </div>

      {error && <div className="mb-4 bg-red-50 text-red-600 p-4 rounded-2xl text-sm font-bold border border-red-100">⚠ {error}</div>}

      <form onSubmit={handleSave} className="space-y-8">
        {/* THÔNG TIN CHUNG */}
        <div className="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-6 border-b border-gray-100 bg-gray-50/50">
            <h3 className="font-bold text-gray-800 flex items-center gap-2 text-sm uppercase tracking-wide"><Box size={18}/> Thông tin cơ bản</h3>
          </div>
          <div className="p-8 space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="md:col-span-2">
                <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Tên sản phẩm</label>
                <input type="text" required value={formData.name}
                  onChange={e => setFormData({...formData, name: e.target.value})}
                  className="w-full p-4 rounded-xl border border-gray-200 text-sm font-bold outline-none focus:ring-2 focus:ring-red-500 bg-gray-50 focus:bg-white transition-all"
                  placeholder="Ví dụ: iPhone 15 Pro Max"
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Danh mục</label>
                <select value={formData.category} onChange={e => setFormData({...formData, category: e.target.value})}
                  className="w-full p-4 rounded-xl border border-gray-200 text-sm font-bold outline-none focus:ring-2 focus:ring-red-500 bg-gray-50 transition-all">
                  <option value="">Chọn danh mục...</option>
                  <option value="Điện thoại">Điện thoại</option>
                  <option value="Laptop">Laptop</option>
                  <option value="Tablet">Tablet</option>
                  <option value="Âm thanh">Âm thanh</option>
                  <option value="Đồng hồ">Đồng hồ</option>
                </select>
              </div>
              <div>
                <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Hãng sản xuất</label>
                <input type="text" value={formData.brand}
                  onChange={e => setFormData({...formData, brand: e.target.value})}
                  className="w-full p-4 rounded-xl border border-gray-200 text-sm font-bold outline-none focus:ring-2 focus:ring-red-500 bg-gray-50 transition-all"
                  placeholder="Apple, Samsung..."
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Ảnh đại diện sản phẩm</label>
              <div className="flex items-center gap-6">
                <div className="w-32 h-32 border-2 border-dashed border-gray-200 rounded-2xl overflow-hidden bg-gray-50 flex items-center justify-center relative group">
                  {formData.imageURL ? (
                    <img src={formData.imageURL.startsWith('http') ? formData.imageURL : `http://localhost:8080${formData.imageURL}`} alt="Preview" className="w-full h-full object-contain" />
                  ) : (
                    <ImageIcon className="text-gray-300" size={32} />
                  )}
                  {uploading === 'main' && (
                    <div className="absolute inset-0 bg-white/80 flex items-center justify-center">
                      <Loader size={20} className="animate-spin text-red-500" />
                    </div>
                  )}
                </div>
                <div className="flex-1">
                  <label className="inline-flex items-center gap-2 px-6 py-3 bg-white border border-gray-200 rounded-xl text-sm font-bold text-gray-700 cursor-pointer hover:bg-gray-50 transition-all shadow-sm">
                    <UploadCloud size={18} />
                    {formData.imageURL ? 'Thay đổi ảnh' : 'Tải ảnh lên'}
                    <input type="file" className="hidden" accept="image/*" onChange={e => e.target.files?.[0] && handleFileUpload(e.target.files[0], 'main')} />
                  </label>
                  <p className="mt-2 text-xs text-gray-400">Hỗ trợ JPG, PNG, WEBP. Dung lượng tối đa 5MB.</p>
                </div>
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Mô tả sản phẩm</label>
              <textarea rows={4} value={formData.description}
                onChange={e => setFormData({...formData, description: e.target.value})}
                className="w-full p-4 rounded-xl border border-gray-200 text-sm font-medium outline-none focus:ring-2 focus:ring-red-500 bg-gray-50 transition-all"
                placeholder="Nhập mô tả sản phẩm..."
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-gray-500 uppercase mb-2">Thông số kỹ thuật (Specs)</label>
              <textarea rows={6} value={formData.specs}
                onChange={e => setFormData({...formData, specs: e.target.value})}
                className="w-full p-4 rounded-xl border border-gray-200 text-sm font-medium outline-none focus:ring-2 focus:ring-red-500 bg-gray-50 transition-all font-mono"
                placeholder="Ví dụ: Chip: M3 Max\nRAM: 36GB\nSSD: 1TB..."
              />
            </div>
          </div>
        </div>

        {/* PHẦN BIẾN THỂ */}
        <div className="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden">
          <div className="p-6 border-b border-gray-100 bg-gray-50/50 flex justify-between items-center">
            <h3 className="font-bold text-gray-800 flex items-center gap-2 text-sm uppercase tracking-wide"><Smartphone size={18}/> Các biến thể sản phẩm</h3>
            <button type="button" onClick={addVariant} className="text-blue-600 hover:text-blue-700 text-sm font-bold flex items-center gap-1">
              <Plus size={16} /> Thêm biến thể
            </button>
          </div>
          
          <div className="divide-y divide-gray-100">
            {variants.map((variant, index) => (
              <div key={index} className="p-8 group relative bg-white hover:bg-gray-50/30 transition-colors">
                <div className="flex justify-between items-start mb-6">
                  <span className="px-3 py-1 bg-gray-900 text-white text-[10px] font-black rounded-lg uppercase">Mẫu #{index + 1}</span>
                  {variants.length > 1 && (
                    <button type="button" onClick={() => removeVariant(index)} className="text-red-400 hover:text-red-600 p-1">
                      <Trash2 size={18} />
                    </button>
                  )}
                </div>

                <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                  {/* Left: Image Upload for Variant */}
                  <div className="flex flex-col items-center gap-3">
                    <div className="w-24 h-24 border-2 border-dashed border-gray-200 rounded-xl overflow-hidden bg-white flex items-center justify-center relative">
                      {variant.imageURL ? (
                        <img src={variant.imageURL.startsWith('http') ? variant.imageURL : `http://localhost:8080${variant.imageURL}`} alt="Variant" className="w-full h-full object-contain" />
                      ) : (
                        <ImageIcon className="text-gray-200" size={24} />
                      )}
                      {uploading === `variant-${index}` && (
                        <div className="absolute inset-0 bg-white/80 flex items-center justify-center">
                          <Loader size={16} className="animate-spin text-blue-500" />
                        </div>
                      )}
                    </div>
                    <label className="text-[10px] font-bold text-blue-600 cursor-pointer uppercase hover:underline">
                      Chọn ảnh
                      <input type="file" className="hidden" onChange={e => e.target.files?.[0] && handleFileUpload(e.target.files[0], 'variant', index)} />
                    </label>
                  </div>

                  {/* Mid: Fields */}
                  <div className="md:col-span-3 grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Màu sắc</label>
                      <input type="text" value={variant.color} onChange={e => handleVariantChange(index, 'color', e.target.value)}
                        className="w-full p-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white" placeholder="Titan" />
                    </div>
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Dung lượng</label>
                      <input type="text" value={variant.storage} onChange={e => handleVariantChange(index, 'storage', e.target.value)}
                        className="w-full p-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white" placeholder="256GB" />
                    </div>
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Mã SKU (Auto)</label>
                      <input type="text" value={variant.sku} onChange={e => handleVariantChange(index, 'sku', e.target.value)}
                        className="w-full p-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white font-mono" />
                    </div>
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Giá gốc (VNĐ)</label>
                      <div className="relative">
                        <DollarSign size={14} className="absolute left-3 top-3.5 text-gray-400" />
                        <input type="number" value={variant.originalPrice} onChange={e => handleVariantChange(index, 'originalPrice', e.target.value)}
                          className="w-full pl-8 pr-3 py-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Giá khuyến mãi (VNĐ)</label>
                      <div className="relative">
                        <DollarSign size={14} className="absolute left-3 top-3.5 text-gray-400" />
                        <input type="number" value={variant.discountedPrice} onChange={e => handleVariantChange(index, 'discountedPrice', e.target.value)}
                          className="w-full pl-8 pr-3 py-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white" />
                      </div>
                    </div>
                    <div>
                      <label className="block text-[10px] font-black text-gray-400 uppercase mb-1">Tồn kho ban đầu</label>
                      <input type="number" value={variant.stockQuantity} onChange={e => handleVariantChange(index, 'stockQuantity', e.target.value)}
                        className="w-full p-3 rounded-lg border border-gray-200 text-sm font-bold outline-none focus:border-blue-500 bg-white" />
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* ACTIONS */}
        <div className="p-6 bg-white border border-gray-100 rounded-3xl shadow-lg flex justify-end gap-4 sticky bottom-6 z-10 mx-auto">
          <button type="button" onClick={() => navigate('/admin/products')}
            className="px-6 py-3 rounded-xl font-bold text-sm text-gray-600 bg-white border border-gray-200 hover:bg-gray-50 transition-all">
            Hủy bỏ
          </button>
          <button type="submit" disabled={saving || !!uploading}
            className="px-8 py-3 rounded-xl font-bold text-sm text-white bg-red-600 hover:bg-red-700 shadow-lg shadow-red-200 transition-all flex items-center gap-2 disabled:opacity-70">
            {saving ? <Loader size={18} className="animate-spin" /> : <Save size={18} />} Lưu sản phẩm & Biến thể
          </button>
        </div>
      </form>
    </div>
  );
};
