import React from "react";

function ProductForm({
  product,
  category,
  OnProductInfoChange,
  OnMainImageChange,
  OnImageDescriptionChange,
  OnSubmitProduct,
}) {
  return (
    <section className="bg-white dark:bg-gray-900 rounded-lg overflow-auto">
      <div className="py-8 px-6 mx-auto max-w-2xl ">
        <h2 className="mb-4 text-xl font-bold text-gray-900 dark:text-white">
          Thêm mặt hàng mới
        </h2>
        <form action="#">
          <div className="grid gap-4 sm:grid-cols-2 sm:gap-6">
            <div className="sm:col-span-2">
              <label
                for="name"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Tên mặt hàng
              </label>
              <input
                type="text"
                name="name"
                id="name"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="Type product name"
                onChange={(e) => OnProductInfoChange(e)}
                value={product.name}
              />
            </div>
            <div className="w-full">
              <label
                for="brand"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Thông số kỹ thuật
              </label>
              <input
                type="text"
                name="technicalDetails"
                id="brand"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="Product brand"
                value={product.technicalDetails}
                onChange={(e) => OnProductInfoChange(e)}
              />
            </div>
            <div className="w-full">
              <label
                for="price"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Giá
              </label>
              <input
                type="number"
                name="price"
                id="price"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="$2999"
                value={product.price}
                onChange={(e) => OnProductInfoChange(e)}
              />
            </div>
            <div>
              <label
                for="categoryId"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Danh mục
              </label>
              <select
                id="category"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                name="categoryId"
                onChange={(e) => OnProductInfoChange(e)}
              >
                {category.map((item, index) => (
                  <option key={index} value={item.id}>
                    {item.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label
                for="itemColor"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Màu sắc
              </label>
              <input
                type="text"
                name="color"
                id="itemColor"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="color..."
                value={product.color}
                onChange={(e) => OnProductInfoChange(e)}
              />
            </div>
            <div className="sm:col-span-2">
              <label
                for="mainImage"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Hình ảnh
              </label>
              <input
                type="file"
                name="productMainImage"
                id="mainImage"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                required=""
                onChange={(e) => OnMainImageChange(e)}
              />
            </div>
            <div className="sm:col-span-2">
              <label
                for="imagesDesc"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Ảnh bổ sung
              </label>
              <input
                type="file"
                multiple
                name="productImageDescription"
                id="imagesDesc"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                required=""
                onChange={(e) => OnImageDescriptionChange(e)}
              />
            </div>
            <div className="sm:col-span-2">
              <label
                for="description"
                className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
              >
                Description
              </label>
              <textarea
                id="description"
                rows="3"
                className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                name="description"
                placeholder="Your description here"
                value={product.description}
                onChange={(e) => OnProductInfoChange(e)}
              ></textarea>
            </div>
          </div>
          <button
            type="submit"
            className="inline-flex items-center px-5 py-2.5 mt-4 sm:mt-6 text-sm font-medium text-center text-white bg-blue-700 rounded-lg focus:ring-4 focus:ring-primary-200 dark:focus:ring-primary-900 hover:bg-blue-800"
            onClick={() => OnSubmitProduct()}
          >
            Add product
          </button>
        </form>
      </div>
    </section>
  );
}

export default ProductForm;
