import React from "react";
import Pagination from "../common/Pagination";
import { Link } from "react-router-dom";


function ProductList({ props, pagination, OnPageClick }) {
  return (
    <div id="product-list" className="px-10">
      <div className="grid grid-cols-4 gap-4">
        {props.map((item, index) => (
          <div
            id="card"
            className="bg-white rounded-xl shadow-lg hover:shadow-slate-400 text-center"
            key={index}
          >
            <img className="w-full h-2/3 object-contain" src={item.image} />

            <div className="p-6 text-center">
              <p className="font-medium text-xl line-clamp-1">{item.name}</p>
              <p className="mt-2">
                {item.price.toLocaleString("vi-VN", {
                  style: "currency",
                  currency: "VND",
                })}
              </p>
            </div>
            <Link
              className="py-2 px-5 bg-teal-500 text-white font-semibold rounded-full shadow-md hover:bg-teal-400 hover:cursor-pointer focus:outline-none focus:ring focus:ring-violet-400 focus:ring-opacity-75"
              to={`/productDetail/${item.id}`}
            >
              Xem thêm
            </Link>
          </div>
        ))}
      </div>
      <Pagination pagination={pagination} OnPageClick={OnPageClick} />
    </div>
  );
}

export default ProductList;
