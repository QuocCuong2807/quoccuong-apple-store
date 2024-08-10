import React from "react";

function MacFilter({ OnProductSeriesChange, OnPriceFilterChange }) {
  return (
    <div id="filter" className="flex justify-between items-center p-10">
      <ul className="flex gap-6">
        <li
          className="hover:cursor-pointer hover:text-teal-600 hover:underline hover:underline-offset-8
           hover:decoration-teal-200"
        >
          <a name="ALL" onClick={(e) => OnProductSeriesChange(e)}>
            Tất cả
          </a>
        </li>
        <li
          className="hover:cursor-pointer hover:text-teal-600 hover:underline hover:underline-offset-8
           hover:decoration-teal-200"
        >
          <a name="M1" onClick={(e) => OnProductSeriesChange(e)}>
            MacBook M1
          </a>
        </li>
        <li
          className="hover:cursor-pointer hover:text-teal-600 hover:underline hover:underline-offset-8
           hover:decoration-teal-200"
        >
          <a name="M2" onClick={(e) => OnProductSeriesChange(e)}>
            MacBook M2
          </a>
        </li>
        <li
          className="hover:cursor-pointer hover:text-teal-600 hover:underline hover:underline-offset-8
           hover:decoration-teal-200"
        >
          <a name="M3" onClick={(e) => OnProductSeriesChange(e)}>
            MacBook M3
          </a>
        </li>
      </ul>
      <div class="max-w-sm ">
        <select
          id="countries"
          class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
             focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400
              dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 hover:cursor-pointer"
          onChange={(e) => OnPriceFilterChange(e)}
        >
          <option value="ASC">Giá thấp đến cao</option>
          <option value="DESC">Giá cao đến thấp</option>
        </select>
      </div>
    </div>
  );
}

export default MacFilter;
