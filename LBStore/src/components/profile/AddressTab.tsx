import React from 'react';
import { Plus } from 'lucide-react';

interface AddressTabProps {
  addresses: any[];
  showAddAddress: boolean;
  setShowAddAddress: (show: boolean) => void;
  newAddress: any;
  setNewAddress: (addr: any) => void;
  handleAddAddress: (e: React.FormEvent) => void;
  handleDeleteAddress: (id: number) => void;
}

export const AddressTab = ({
  addresses, showAddAddress, setShowAddAddress,
  newAddress, setNewAddress, handleAddAddress, handleDeleteAddress
}: AddressTabProps) => {
  return (
    <div className="bg-white p-8 rounded-[2rem] shadow-sm border border-gray-100 animate-in fade-in slide-in-from-right-4">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-black text-gray-800 uppercase italic">Sổ địa chỉ</h2>
        <button
          onClick={() => setShowAddAddress(!showAddAddress)}
          className="flex items-center gap-2 bg-gray-900 text-white px-4 py-2 rounded-xl text-xs font-bold hover:bg-red-600 transition-all"
        >
          <Plus size={16} /> {showAddAddress ? 'Hủy bỏ' : 'Thêm địa chỉ mới'}
        </button>
      </div>

      {showAddAddress && (
        <form onSubmit={handleAddAddress} className="mb-8 bg-gray-50 p-6 rounded-2xl border border-gray-200 animate-in fade-in slide-in-from-top-2">
          <h3 className="font-bold text-gray-800 mb-4 uppercase text-sm">Nhập thông tin địa chỉ mới</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
            <input
              type="text" placeholder="Họ và tên người nhận" required
              value={newAddress.name}
              onChange={e => setNewAddress({ ...newAddress, name: e.target.value })}
              className="p-3 rounded-xl border border-gray-200 text-sm font-medium outline-none focus:ring-2 focus:ring-red-500"
            />
            <input
              type="tel" placeholder="Số điện thoại" required
              value={newAddress.phone}
              onChange={e => setNewAddress({ ...newAddress, phone: e.target.value })}
              className="p-3 rounded-xl border border-gray-200 text-sm font-medium outline-none focus:ring-2 focus:ring-red-500"
            />
            <input
              type="text" placeholder="Địa chỉ chi tiết (Số nhà, đường, phường/xã...)" required
              value={newAddress.address}
              onChange={e => setNewAddress({ ...newAddress, address: e.target.value })}
              className="md:col-span-2 p-3 rounded-xl border border-gray-200 text-sm font-medium outline-none focus:ring-2 focus:ring-red-500"
            />
          </div>
          <button className="bg-red-600 text-white px-6 py-3 rounded-xl font-bold text-sm shadow-lg shadow-red-100 hover:bg-red-700 transition-all">
            Lưu địa chỉ
          </button>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {addresses.map((addr) => (
          <div key={addr.id} className={`p-6 border-2 ${addr.isDefault ? 'border-red-600 bg-red-50' : 'border-gray-100 hover:border-gray-200'} rounded-[1.5rem] relative transition-all`}>
            <div className="flex items-center gap-2 mb-2">
              <span className="font-bold text-gray-800">{addr.receiverName}</span>
              {addr.isDefault && <span className="bg-red-600 text-white text-[8px] px-1.5 py-0.5 rounded font-black uppercase">Mặc định</span>}
            </div>
            <p className="text-sm text-gray-600 leading-relaxed font-bold">{addr.receiverPhone}</p>
            <p className="text-sm text-gray-600 leading-relaxed mt-1">{addr.streetAddress}</p>
            {!addr.isDefault && (
              <button
                onClick={() => handleDeleteAddress(addr.id)}
                className="absolute top-4 right-4 text-xs font-bold text-gray-400 hover:text-red-600"
              >
                Xóa
              </button>
            )}
          </div>
        ))}
        {addresses.length === 0 && !showAddAddress && (
          <div className="col-span-1 md:col-span-2 text-center py-8 text-gray-400 italic">Chưa có địa chỉ giao hàng nào. Hãy thêm một địa chỉ.</div>
        )}
      </div>
    </div>
  );
};
