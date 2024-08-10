import React from "react";
import { CSVLink } from "react-csv";

function OrderDetail({ props }) {
  const csv = props.orderDetails.map((item) => ({
    name: item.product.name,
    color: item.product.color,
    technicalDetails: item.product.technicalDetails,
    quantity: item.quantity,
    price: item.price,
  }));


  return (
    <div className="p-8 rounded-lg bg-white shadow-lg">
      <h1 className="font-bold font-mono text-2xl">Chi tiết đơn hàng</h1>
      <div className="mt-4">
        <div className="flex items-center gap-2 mt-2">
          <h1 className="font-medium text-sm">Họ tên người nhận:</h1>
          <p className="text-sm">{props.customerFullName}</p>
        </div>
        <div className="flex items-center gap-2 mt-2">
          <h1 className="font-medium text-sm">Số điện thoại:</h1>
          <p className="text-sm">{props.phoneNumber}</p>
        </div>
        <div className="flex items-center gap-2 mt-2">
          <h1 className="font-medium text-sm">Email:</h1>
          <p className="text-sm">{props.email}</p>
        </div>
        <div className="flex items-center gap-2 mt-2">
          <h1 className="font-medium text-sm">Địa chỉ:</h1>
          <p className="text-sm">{props.address}</p>
        </div>
        <div className="flex items-center gap-2 mt-2">
          <h1 className="font-medium text-sm">Tổng tiền:</h1>
          <p className="text-sm">
            {props.totalPrice.toLocaleString("vi-VN", {
              style: "currency",
              currency: "VND",
            })}
          </p>
        </div>
        <div className="mt-4">
          <h1 className="font-medium text-sm">Danh sách mặt hàng:</h1>
          <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-4 py-3">
                  Tên
                </th>
                <th scope="col" className="px-4 py-3">
                  Thông số
                </th>
                <th scope="col" className="px-4 py-3">
                  Màu sắc
                </th>
                <th scope="col" className="px-4 py-3">
                  SL
                </th>
                <th scope="col" className="px-4 py-3">
                  Đơn giá
                </th>
              </tr>
            </thead>
            <tbody>
              {props.orderDetails.map((item, index) => (
                <tr className="border-b dark:border-gray-700">
                  <th
                    scope="row"
                    className="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                  >
                    {item.product.name}&#34;
                  </th>
                  <td className="px-4 py-3">{item.product.technicalDetails}</td>
                  <td className="px-4 py-3">{item.product.color}</td>
                  <td className="px-4 py-3">{item.quantity}</td>
                  <td className="px-4 py-3">
                    {item.price.toLocaleString("vi-VN", {
                      style: "currency",
                      currency: "VND",
                    })}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="flex justify-end mt-4">
          <CSVLink data={csv} className="font-medium text-blue-600 underline hover:cursor-pointer">
            Xuất file csv...
          </CSVLink>
        </div>
      </div>
    </div>
  );
}

export default OrderDetail;
