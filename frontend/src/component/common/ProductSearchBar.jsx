import React from "react";
import { FaDeleteLeft } from "react-icons/fa6";
function ProductSearchBar({
  searchValue,
  OnSearchNameChange,
  OnResetSearchName,
}) {
  return (
    <div>
      <div className="relative w-max mx-auto">
        <input
          className="relative pl-6 pr-4 z-10 bg-transparent cursor-pointer w-10 h-10 rounded-full border focus:w-full
             focus:border-cyan-500 outline-none 
              focus:cursor-text focus:pl-10 focus:pr-4"
          placeholder="Search your items..."
          value={searchValue}
          onChange={(e) => OnSearchNameChange(e)}
          onBlur={() => OnResetSearchName()}
        ></input>

        <svg
          data-slot="icon"
          fill="none"
          stroke-width="1.5"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
          aria-hidden="true"
          className="absolute w-8 h-6 inset-y-0 my-auto px-2 ml-1 peer-focus:border-cyan-500 peer-focus:stroke-cyan-500"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"
          ></path>
        </svg>
      </div>
    </div>
  );
}

export default ProductSearchBar;
