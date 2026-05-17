import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { ChevronLeft, Save } from 'lucide-react';
import { updateVoucher, fetchVoucherById } from '../../services/api';

export const AdminVoucherEdit = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        code: '',
        discountPercentage: 0,
        maxDiscountAmount: 0,
        minOrderValue: 0,
        usageLimit: 0,
        isActive: true,
        startDate: '',
        endDate: '',
        description: ''
    });

    useEffect(() => {
        if (id) {
            loadVoucher();
        }
    }, [id]);

    const loadVoucher = async () => {
        const data = await fetchVoucherById(id as string);
        if (data) {
            setFormData({
                code: data.code,
                discountPercentage: data.discountPercentage,
                maxDiscountAmount: data.maxDiscountAmount || 0,
                minOrderValue: data.minOrderValue || 0,
                usageLimit: data.usageLimit || 0,
                isActive: data.isActive,
                startDate: data.startDate ? data.startDate.split('T')[0] : '',
                endDate: data.endDate ? data.endDate.split('T')[0] : '',
                description: data.description || ''
            });
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        const finalValue = type === 'checkbox' ? (e.target as HTMLInputElement).checked : value;
        setFormData(prev => ({ ...prev, [name]: finalValue }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        try {
            const payload = {
                ...formData,
                startDate: formData.startDate ? formData.startDate + 'T00:00:00' : null,
                endDate: formData.endDate ? formData.endDate + 'T23:59:59' : null,
            };
            await updateVoucher(id as string, payload);
            navigate('/admin/vouchers');
        } catch (error: any) {
            alert(error.message || 'Lỗi cập nhật voucher');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-3xl mx-auto space-y-6">
            <div className="flex items-center gap-4">
                <Link to="/admin/vouchers" className="p-2 hover:bg-gray-100 rounded-xl transition">
                    <ChevronLeft size={24} />
                </Link>
                <div>
                    <h1 className="text-2xl font-bold text-gray-800">Cập nhật Voucher</h1>
                    <p className="text-gray-500 mt-1">Chỉnh sửa mã giảm giá {formData.code}</p>
                </div>
            </div>

            <form onSubmit={handleSubmit} className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 md:p-8 space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Mã Voucher</label>
                        <input
                            type="text"
                            name="code"
                            disabled
                            className="w-full px-4 py-2 border border-gray-200 bg-gray-50 rounded-xl outline-none"
                            value={formData.code}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Phần trăm giảm (%) *</label>
                        <input
                            type="number"
                            name="discountPercentage"
                            required
                            min="0"
                            max="100"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.discountPercentage}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Đơn tối thiểu (VNĐ)</label>
                        <input
                            type="number"
                            name="minOrderValue"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.minOrderValue}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Giảm tối đa (VNĐ)</label>
                        <input
                            type="number"
                            name="maxDiscountAmount"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.maxDiscountAmount}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Giới hạn sử dụng (0 = Không giới hạn)</label>
                        <input
                            type="number"
                            name="usageLimit"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.usageLimit}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="flex items-center gap-3 mt-8">
                        <input
                            type="checkbox"
                            name="isActive"
                            id="isActive"
                            className="w-5 h-5 accent-red-600 rounded cursor-pointer"
                            checked={formData.isActive}
                            onChange={handleChange}
                        />
                        <label htmlFor="isActive" className="text-sm font-medium text-gray-700 cursor-pointer">
                            Kích hoạt Voucher
                        </label>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Ngày bắt đầu</label>
                        <input
                            type="date"
                            name="startDate"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.startDate}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">Ngày kết thúc</label>
                        <input
                            type="date"
                            name="endDate"
                            className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                            value={formData.endDate}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Mô tả chi tiết</label>
                    <textarea
                        name="description"
                        rows={3}
                        className="w-full px-4 py-2 border border-gray-200 rounded-xl focus:border-red-500 outline-none"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Mô tả về voucher này..."
                    />
                </div>

                <div className="flex justify-end pt-4">
                    <button
                        type="submit"
                        disabled={loading}
                        className="flex items-center gap-2 bg-red-600 text-white px-6 py-2.5 rounded-xl hover:bg-red-700 transition disabled:opacity-50"
                    >
                        <Save size={20} />
                        <span>{loading ? 'Đang cập nhật...' : 'Cập nhật'}</span>
                    </button>
                </div>
            </form>
        </div>
    );
};
