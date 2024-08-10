import React from "react";
import ReactPaginate from "react-paginate";
import { BsChevronLeft, BsChevronRight } from "react-icons/bs";
function Pagination({pagination, OnPageClick }) {
  return (
    <div>
      <ReactPaginate
        breakLabel={<span className="mr-4">...</span>}
        nextLabel={
          <span className="w-10 h-10 flex items-center justify-center bg-[#D3D3D3] rounded-md ml-4">
            <BsChevronRight />
          </span>
        }
        onPageChange={(e) => OnPageClick(e)}
        pageRangeDisplayed={2}
        pageCount={pagination.totalPage}
        previousLabel={
          <span className="w-10 h-10 flex items-center justify-center bg-[#D3D3D3] rounded-md mr-4">
            <BsChevronLeft />
          </span>
        }
        renderOnZeroPageCount={null}
        containerClassName="flex items-center justify-center mt-8 mb-4"
        pageClassName="block border-solid border-slate-200 hover:bg-[#D3D3D3] w-10 h-10 flex items-center justify-center rounded-md mr-4"
        activeClassName="bg-[#6842EF] text-white"
      />
    </div>
  );
}

export default Pagination;
