import React from 'react'

function EditCategoryForm({props, OnCategoryChange, SubmitEditCategory}) {
  return (
    <section className="bg-white dark:bg-gray-900 rounded-lg">
      <div className="py-8 px-6 mx-auto">
        <h2 className="mb-4 text-xl font-bold text-gray-900 dark:text-white">
          Chỉnh sửa thông tin
        </h2>
        <form action="#">
          <div className="grid gap-4 sm:grid-cols-2 sm:gap-6 ">
            <div className="sm:col-span-2 ">
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
                className="pe-36 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                placeholder="Type product name"
                required=""
                value={props.name}
                onChange={(e) => OnCategoryChange(e)}
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
                name="description"
                rows="5"
                className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-primary-500 focus:border-primary-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="Your description here"
                value={props.description}
                onChange={(e) => OnCategoryChange(e)}
              />
            </div>
          </div>
          <button
            type="submit"
            className="inline-flex items-center px-5 py-2.5 mt-4 sm:mt-6 text-sm font-medium text-center text-white bg-yellow-500 rounded-lg focus:ring-4 hover:bg-yellow-700"
            onClick={() => SubmitEditCategory()}
          >
            Edit
          </button>
        </form>
      </div>
    </section>
  )
}

export default EditCategoryForm
