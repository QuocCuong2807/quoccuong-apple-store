import React from "react";
import { MdOutlineAttachMoney } from "react-icons/md";
import { AiOutlineProduct } from "react-icons/ai";
import { IoDocumentsOutline } from "react-icons/io5";

function TotalOverview({ Revenue, ProductQuantity, OrderQuantity }) {
  return (
    <div className="p-6">
      <div className="grid grid-cols-3 gap-4">
        <div className="border rounded-lg shadow-lg p-6">
          <div className="rounded-full w-10 h-10 flex justify-center items-center bg-emerald-200">
            <MdOutlineAttachMoney />
          </div>
          <div className="mt-4">
            <h1 className="font-bold text-xl">
              {" "}
              {Revenue.toLocaleString("vi-VN", {
                style: "currency",
                currency: "VND",
              })}
            </h1>
            <p className="mt-2 text-slate-500 text-sm">Tổng doanh thu</p>
          </div>
        </div>

        <div className="border rounded-lg shadow-lg p-6">
          <div className="rounded-full w-10 h-10 flex justify-center items-center bg-emerald-200">
            <AiOutlineProduct />
          </div>
          <div className="mt-4">
            <h1 className="font-bold text-xl"> {ProductQuantity}</h1>
            <p className="mt-2 text-slate-500 text-sm">Tổng sản phẩm</p>
          </div>
        </div>

        <div className="border rounded-lg shadow-lg p-6">
          <div className="rounded-full w-10 h-10 flex justify-center items-center bg-emerald-200">
            <IoDocumentsOutline />
          </div>
          <div className="mt-4">
            <h1 className="font-bold text-xl"> {OrderQuantity}</h1>
            <p className="mt-2 text-slate-500 text-sm">Tổng đơn hàng</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default TotalOverview;
