import React from "react";
import { FaRegTrashAlt } from "react-icons/fa";
import store from "../../store/store";
import { handleAddToCart, handleMinusItemQuantity, handleRemoveCartItem } from "../../store/action";
function CartItems({ cartItem }) {
  return (
    <div className="col-span-2 px-6">
      <h1 className="font-mono font-medium text-3xl">Shopping Cart</h1>

      <div className="mt-6">
        {cartItem.map((item, index) => (
          <div
            key={index}
            className="bg-white flex justify-between items-center gap-6 px-6 py-2 rounded-xl border my-2 shadow-md"
          >
            <div>
              <img src={item.image} className="object-contain" />
            </div>

            <div className="flex items-center">
              <div>
                <p className="font-medium">{item.name}</p>
                <div className="flex items-center gap-4 mt-4">
                  <span className="text-red-500 flex items-center hover:cursor-pointer"
                  onClick={() => store.dispatch(handleRemoveCartItem(item))}>
                    <FaRegTrashAlt />
                    Remove
                  </span>
                </div>
              </div>
            </div>
            <div className="flex justify-center items-center">
              <button
                type="button"
                id="decrement-button"
                data-input-counter-decrement="counter-input"
                className="inline-flex h-5 w-5 shrink-0 items-center justify-center rounded-md border border-gray-300 bg-gray-100 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-100 dark:border-gray-600 dark:bg-gray-700 dark:hover:bg-gray-600 dark:focus:ring-gray-700"
                onClick={() => store.dispatch(handleMinusItemQuantity(item))}
              >
                <svg
                  className="h-2.5 w-2.5 text-gray-900 dark:text-white"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 18 2"
                >
                  <path
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M1 1h16"
                  />
                </svg>
              </button>
              <input
                type="text"
                id="counter-input"
                data-input-counter
                className="w-10 shrink-0 border-0 bg-transparent text-center text-sm font-medium text-gray-900 focus:outline-none focus:ring-0 dark:text-white"
                placeholder=""
                value={item.quantity}
                required
              />
              <button
                type="button"
                id="increment-button"
                data-input-counter-increment="counter-input"
                className="inline-flex h-5 w-5 shrink-0 items-center justify-center rounded-md border border-gray-300 bg-gray-100 hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-100 dark:border-gray-600 dark:bg-gray-700 dark:hover:bg-gray-600 dark:focus:ring-gray-700"
                onClick={() => store.dispatch(handleAddToCart(item))}
              >
                <svg
                  className="h-2.5 w-2.5 text-gray-900 dark:text-white"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 18 18"
                >
                  <path
                    stroke="currentColor"
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M9 1v16M1 9h16"
                  />
                </svg>
              </button>
            </div>
            <p className="font-medium text-xl">
              {item.totalPrice().toLocaleString("vi-VN", {
                style: "currency",
                currency: "VND",
              })}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default CartItems;
