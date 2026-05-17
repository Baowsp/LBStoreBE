import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import { Link } from 'react-router-dom';
import { fetchBanners, deleteBanner } from '../../services/api';

export const AdminBanners = () => {
  const [banners, setBanners] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const loadBanners = async () => {
    setIsLoading(true);
    try {
      const data = await fetchBanners();
      setBanners(data);
    } catch (error) {
      console.error("Failed to fetch banners", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadBanners();
  }, []);

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa banner này khỏi thư viện? (Lưu ý: Nếu đang dùng hiển thị sẽ bị lỗi)')) {
      const success = await deleteBanner(id);
      if (success) {
        setBanners(banners.filter(b => b.id !== id));
      } else {
        alert("Xóa banner thất bại!");
      }
    }
  };

  if (isLoading) return <div className="p-8 text-center text-gray-500">Đang tải dữ liệu thư viện...</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div>
          <h1 className="text-2xl font-black text-gray-800 uppercase italic">Thư viện Banner</h1>
          <p className="text-sm text-gray-500 mt-1">Quản lý toàn bộ hình ảnh quảng cáo để có thể tái sử dụng cho giao diện</p>
        </div>
        <Link 
          to="/admin/banners/add" 
          className="flex items-center gap-2 bg-blue-600 text-white px-5 py-2.5 rounded-xl font-bold hover:bg-blue-700 transition-colors shadow-lg shadow-blue-200"
        >
          <Plus size={20} /> Thêm ảnh mới
        </Link>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-black text-gray-500 uppercase tracking-wider">Hình ảnh</th>
                <th className="px-6 py-4 text-left text-xs font-black text-gray-500 uppercase tracking-wider">Tiêu đề</th>
                <th className="px-6 py-4 text-left text-xs font-black text-gray-500 uppercase tracking-wider">Vị trí dự kiến</th>
                <th className="px-6 py-4 text-left text-xs font-black text-gray-500 uppercase tracking-wider">Trạng thái</th>
                <th className="px-6 py-4 text-right text-xs font-black text-gray-500 uppercase tracking-wider">Thao tác</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {banners.map((banner) => (
                <tr key={banner.id} className="hover:bg-gray-50/50 transition-colors">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <img 
                      src={banner.imageUrl} 
                      alt={banner.title}
                      className="h-16 w-32 object-cover rounded-lg border border-gray-200"
                    />
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="font-bold text-gray-800">{banner.title || 'Không có tiêu đề'}</div>
                    <div className="text-xs text-blue-500 max-w-[200px] truncate">{banner.targetUrl}</div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="px-3 py-1 bg-gray-100 text-gray-600 rounded-lg text-xs font-bold border border-gray-200">
                      {banner.position}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-3 py-1 rounded-lg text-xs font-bold border ${banner.active ? 'bg-green-50 text-green-600 border-green-200' : 'bg-red-50 text-red-600 border-red-200'}`}>
                      {banner.active ? 'Kích hoạt' : 'Tạm ẩn'}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    <div className="flex items-center justify-end gap-2">
                      <Link
                        to={`/admin/banners/edit/${banner.id}`}
                        className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                      >
                        <Edit size={18} />
                      </Link>
                      <button
                        onClick={() => handleDelete(banner.id)}
                        className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                      >
                        <Trash2 size={18} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
              {banners.length === 0 && (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-gray-500 font-medium">
                    Chưa có banner nào trong thư viện.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};