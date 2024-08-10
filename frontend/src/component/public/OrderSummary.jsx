import React from "react";
import { Link } from "react-router-dom";

function OrderSummary({ totalPrice }) {
  console.log(totalPrice);
  return (
    <div className="px-4">
      <h3 className="font-mono font-medium text-3xl">Order Summary</h3>

      <div className="bg-white border rounded-xl p-4 mt-6 grid grid-rows gap-y-2">
        <div className="flex items-center justify-between">
          <p className="">Giá đơn hàng:</p>
          <p className="font-medium text-blue-600">
            {totalPrice.toLocaleString("vi-VN", {
              style: "currency",
              currency: "VND",
            })}
          </p>
        </div>
        <div className="flex items-center justify-between">
          <p className="">Giảm giá:</p>
          <p className="font-medium text-blue-600">0</p>
        </div>
        <div className="flex items-center justify-between">
          <p className="font-bold">Tổng tiền:</p>
          <p className="font-bold text-blue-600">
            {totalPrice.toLocaleString("vi-VN", {
              style: "currency",
              currency: "VND",
            })}
          </p>
        </div>

        <div className="flex items-center justify-between">
          <label
            for="voucher"
            className="mb-2 block text-base font-medium text-gray-900 dark:text-white"
          >
            Mã giảm giá:
          </label>
          <div className="flex justify-between items-center gap-4">
            <input
              type="text"
              id="voucher"
              className="rounded-lg border border-black"
              placeholder=""
              required
            />
            <button
              type="button"
              className="text-white bg-blue-500 px-4 py-1 rounded-lg"
            >
              Apply
            </button>
          </div>
        </div>
        <div className="border my-4"></div>
        <Link to="/checkOut" className="text-center">
          <button
            type="button"
            className="text-white bg-gradient-to-r from-cyan-500 to-blue-500 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-cyan-300 dark:focus:ring-cyan-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
          >
            Tiến hành đặt hàng
          </button>
        </Link>
      </div>
    </div>
  );
}

export default OrderSummary;
