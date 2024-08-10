import React from "react";
import { Link } from "react-router-dom";

function ProductOverview({ props }) {
  return (
    <div className="p-10">
      {props.map((item) => (
        <div key={item.categoryId}>
          <h2 className="text-center text-3xl font-bold font-mono my-6">
            {item.categoryName}
          </h2>

          <div className="grid grid-cols-4 gap-4">
            {item.productDtoList.map((item) => (
              <div
                id="card"
                className="bg-white rounded-xl shadow-md hover:shadow-slate-400 text-center"
                key={item.id}
              >
                <img className="w-full h-2/3 object-contain" src={item.image} />

                <div className="p-6 text-center">
                  <p className="font-medium text-xl line-clamp-1">
                    {item.name}
                  </p>
                  <p className="mt-2">
                    {item.price.toLocaleString("vi-VN", {
                      style: "currency",
                      currency: "VND",
                    })}
                  </p>
                </div>
                <Link
                  to={`/productDetail/${item.id}`}
                  class="py-2 px-5 bg-teal-500 text-white font-semibold rounded-full shadow-md hover:bg-teal-400 hover:cursor-pointer focus:outline-none focus:ring focus:ring-violet-400 focus:ring-opacity-75"
                >
                  Xem thêm
                </Link>
              </div>
            ))}
          </div>

          <div className="mt-10 text-center">
            <Link
              to={`/product/${item.categoryId}`}
              class="relative inline-flex items-center justify-center p-0.5 mb-2 me-2 overflow-hidden text-sm font-medium
   text-gray-900 rounded-lg group bg-gradient-to-br from-green-400 to-blue-600 group-hover:from-green-400
    group-hover:to-blue-600 hover:text-white hover:cursor-pointer dark:text-white focus:ring-4 focus:outline-none
     focus:ring-green-200 dark:focus:ring-green-800"
            >
              <span class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white dark:bg-gray-900 rounded-md group-hover:bg-opacity-0">
                Xem thêm iPhone
              </span>
            </Link>
          </div>
        </div>
      ))}
    </div>
  );
}

export default ProductOverview;
