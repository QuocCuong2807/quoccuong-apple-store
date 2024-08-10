import React from "react";
import store from "../../store/store";
import { handleAddToCart } from "../../store/action";

function ProductInfo({ props }) {
  return (
    <div className="col-span-8">
      <div className="pt-6">
        <p className="font-semibold font-mono text-3xl">{props.name}</p>
        <p className="font-bold text-2xl mt-4"></p>
        <div className="mt-4">
          <p className="text-sm text-slate-500 mt-2">
            <b>Thông số: </b> {props.technicalDetails}
          </p>
          <p className="text-sm text-slate-500 mt-2">
            <b>Màu sắc: </b> {props.color}
          </p>
          <p className="text-sm text-slate-500 mt-2">
            <b>Mô tả: </b> {props.description}
          </p>
        </div>
      </div>
      <div className="mt-10">
        <a
          className="relative inline-flex items-center justify-center p-0.5 mb-2 me-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-green-400 to-blue-600 group-hover:from-green-400 group-hover:to-blue-600 hover:text-white hover:cursor-pointer dark:text-white focus:ring-4 focus:outline-none focus:ring-green-200 dark:focus:ring-green-800"
          onClick={() =>
            store.dispatch(
              handleAddToCart({
                id: props.id,
                name: props.name,
                image: props.image,
                price: props.price,
                quantity: props.quantity,
              })
            )
          }
        >
          <span className="relative px-5 py-2.5 flex items-center gap-2 transition-all ease-in duration-75 bg-white dark:bg-gray-900 rounded-md group-hover:bg-opacity-0">
            Thêm vào giỏ hàng{" "}
            <svg
              className="w-5"
              data-slot="icon"
              fill="none"
              stroke-width="1.5"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 0 0-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 0 0-16.536-1.84M7.5 14.25 5.106 5.272M6 20.25a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Zm12.75 0a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z"
              ></path>
            </svg>
          </span>
        </a>
      </div>
    </div>
  );
}

export default ProductInfo;
