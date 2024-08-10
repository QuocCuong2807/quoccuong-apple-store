import React, { useState } from "react";
import Modal from "../common/Modal";
import Pagination from "../common/Pagination";
import ProductForm from "../common/ProductForm";
import { FaRegEdit } from "react-icons/fa";
import { MdDelete } from "react-icons/md";
import { FiPlus } from "react-icons/fi";

function ManageProduct({
  products,
  categories,
  priceFilter,
  OnShowProductModal,
  OnShowEditProductModal,
  pagination,
  OnPageClick,
  OnCategoryFilterChange,
  OnPriceFilterChange,
}) {
  return (
    <div>
      <h1 className="bg-gray-50 font-medium font-mono text-xl px-16 pt-6">
        Quản lý sản phẩm
      </h1>
      <section className="bg-gray-50 dark:bg-gray-900 p-3 sm:p-5">
        <div className="mx-auto max-w-screen-xl px-4 lg:px-12">
          <div className="bg-white dark:bg-gray-800 relative shadow-md sm:rounded-lg overflow-hidden">
            <div className="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0 md:space-x-4 p-4">
              <div className="w-full md:w-1/2"></div>
              <div className="w-full md:w-auto flex items-center flex-col md:flex-row space-y-2 md:space-y-0 justify-end md:space-x-3 flex-shrink-0">
                <div className="flex items-center space-x-3 w-full md:w-auto">
                  <select
                    id="countries"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
             focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400
              dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 hover:cursor-pointer"
                    onChange={(e) => OnCategoryFilterChange(e)}
                  >
                    <option value="ALL">All</option>
                    {categories.map((item, index) => (
                      <option key={index} value={item.id}>
                        {item.name}
                      </option>
                    ))}
                  </select>

                  <select
                    id="countries"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
             focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400
              dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 hover:cursor-pointer"
                    onChange={(e) => OnPriceFilterChange(e)}
                  >
                    <option value="DESC">Giá cao đến thấp</option>
                    <option value="ASC">Giá thấp đến cao</option>
                  </select>
                </div>
                <button
                  onClick={() => OnShowProductModal()}
                  type="button"
                  className="flex items-center justify-center text-white bg-blue-600 hover:bg-blue-800 focus:ring-4 focus:ring-primary-300 font-medium rounded-lg text-sm px-4 py-3 dark:bg-primary-600 dark:hover:bg-primary-700 focus:outline-none dark:focus:ring-primary-800"
                >
                  <FiPlus />
                  Add product
                </button>
              </div>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                  <tr>
                    <th scope="col" className="px-4 py-3">
                      Tên
                    </th>
                    <th scope="col" className="px-4 py-3">
                      Danh mục
                    </th>
                    <th scope="col" className="px-4 py-3">
                      Thông số
                    </th>
                    <th scope="col" className="px-4 py-3">
                      Màu sắc
                    </th>
                    <th scope="col" className="px-4 py-3">
                      Giá
                    </th>
                    <th scope="col" className="px-4 py-3">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {products.map((item, index) => (
                    <tr key={index} className="border-b dark:border-gray-700">
                      <th
                        scope="row"
                        className="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                      >
                        {item.name}&#34;
                      </th>
                      <td className="px-4 py-3">{item.category.name}</td>
                      <td className="px-4 py-3">{item.technicalDetails}</td>
                      <td className="px-4 py-3">{item.color}</td>
                      <td className="px-4 py-3">
                        {item.price.toLocaleString("vi-VN", {
                          style: "currency",
                          currency: "VND",
                        })}
                      </td>
                      <td className="px-4 py-3 flex items-center justify-start gap-4">
                        <button
                          type="button"
                          data-drawer-target="drawer-update-product"
                          data-drawer-show="drawer-update-product"
                          aria-controls="drawer-update-product"
                          className="py-2 px-3 flex items-center gap-2 text-sm font-medium text-center text-white bg-yellow-400 rounded-lg hover:bg-yellow-600 focus:ring-4 focus:outline-none"
                          onClick={() => OnShowEditProductModal(item)}
                        >
                          <FaRegEdit />
                          Edit
                        </button>
                        <button
                          type="button"
                          data-modal-target="delete-modal"
                          data-modal-toggle="delete-modal"
                          className="flex items-center gap-2 text-red-700 hover:text-white border border-red-700 hover:bg-red-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm px-3 py-2 text-center dark:border-red-500 dark:text-red-500 dark:hover:text-white dark:hover:bg-red-600 dark:focus:ring-red-900"
                        >
                          <MdDelete />
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <Pagination pagination={pagination} OnPageClick={OnPageClick} />
          </div>
        </div>
      </section>
    </div>
  );
}

export default ManageProduct;
