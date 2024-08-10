import axios from "axios";
import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { useDebounce } from "../../hooks";
import Pagination from "../common/Pagination";
function OrderList({
  OnShowOrderDetail,
  OnFilterChange,
  Orders,
  pagination,
  OnPageClick,
  Token,
}) {
  const [searchedOrder, setSearchedOrder] = useState({});
  const [searchParam, setSearchParam] = useState("");
  const debouncedSearchId = useDebounce(searchParam);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getOrderById(debouncedSearchId);
  }, [debouncedSearchId]);

  const getOrderById = async (orderId) => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/order/order-by-id?id=${orderId}`,
        {
          headers: {
            Authorization: `Bearer ${Token}`,
          },
        }
      );
      setSearchedOrder(response.data);
    } catch (error) {
      setSearchedOrder({});
    }
    setLoading(false);
  };

  return (
    <div>
      <div className="flex flex-col md:flex-row items-center justify-between space-y-3 md:space-y-0 md:space-x-4 p-4">
        <div className="w-full md:w-1/2">
          <div className="relative flex items-center">
            <div className="relative w-full">
              <div className="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
                <svg
                  aria-hidden="true"
                  className="w-5 h-5 text-gray-500 dark:text-gray-400"
                  fill="currentColor"
                  viewbox="0 0 20 20"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fill-rule="evenodd"
                    d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z"
                    clip-rule="evenodd"
                  />
                </svg>
              </div>
              <input
                type="text"
                id="simple-search"
                className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-primary-500 focus:border-primary-500 block w-full pl-10 p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500"
                placeholder="Enter Order id..."
                required=""
                value={searchParam}
                onChange={(e) => setSearchParam(e.target.value)}
                onBlur = {() => setSearchParam("")}
              />
            </div>
            {loading ? (
              <div>Loading</div>
            ) : Object.keys(searchedOrder).length !== 0 ? (
              <div className="absolute bg-white z-10 w-max mx-auto border rounded-lg top-10 overflow-auto shadow-lg">
                <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                  <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                    <tr>
                      <th scope="col" className="px-4 py-3">
                        Mã giao dịch
                      </th>
                      <th scope="col" className="px-4 py-3">
                        Số điện thoại giao hàng
                      </th>
                      <th scope="col" className="px-4 py-3">
                        Tổng tiền
                      </th>
                      <th scope="col" className="px-4 py-3">
                        Ngày lập hoá đơn
                      </th>
                      <th scope="col" className="px-4 py-3">
                        Trạng thái
                      </th>
                      <th scope="col" className="px-4 py-3">
                        Action
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <th
                        scope="row"
                        className="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                      >
                        #{searchedOrder.id}
                      </th>
                      <td className="px-4 py-3">{searchedOrder.phoneNumber}</td>
                      <td className="px-4 py-3">
                        {searchedOrder.totalPrice.toLocaleString("vi-VN", {
                          style: "currency",
                          currency: "VND",
                        })}
                      </td>
                      <td className="px-4 py-3">{searchedOrder.date}</td>
                      <td>
                        {searchedOrder.status === true ? (
                          <div class="flex items-center">
                            <div class="h-2.5 w-2.5 rounded-full bg-green-500 me-2"></div>{" "}
                            Đã thanh toán
                          </div>
                        ) : (
                          <div class="flex items-center">
                            <div class="h-2.5 w-2.5 rounded-full bg-red-500 me-2"></div>{" "}
                            Chưa thanh toán
                          </div>
                        )}
                      </td>
                      <td>
                        <a
                          className="font-medium text-blue-600 underline hover:cursor-pointer"
                          onClick={() => OnShowOrderDetail(searchedOrder)}
                        >
                          Xem chi tiết...
                        </a>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            ) : (
              <></>
            )}
          </div>
        </div>
        <div className="w-full md:w-auto flex flex-col md:flex-row space-y-2 md:space-y-0 items-stretch md:items-center justify-end md:space-x-3 flex-shrink-0">
          <div className="flex items-center space-x-3 w-full md:w-auto">
            <select
              id="countries"
              className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
             focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400
              dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 hover:cursor-pointer"
              onChange={(e) => OnFilterChange(e)}
            >
              <option selected value="ALL">
                All
              </option>
              <option value="1">Đã thanh toán</option>
              <option value="0">Chưa thanh toán</option>
            </select>
          </div>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-sm text-left text-gray-500 dark:text-gray-400">
          <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
            <tr>
              <th scope="col" className="px-4 py-3">
                Mã giao dịch
              </th>
              <th scope="col" className="px-4 py-3">
                Địa chỉ giao hàng
              </th>
              <th scope="col" className="px-4 py-3">
                Số điện thoại giao hàng
              </th>
              <th scope="col" className="px-4 py-3">
                Tổng tiền
              </th>
              <th scope="col" className="px-4 py-3">
                Ngày lập hoá đơn
              </th>
              <th scope="col" className="px-4 py-3">
                Trạng thái
              </th>
              <th scope="col" className="px-4 py-3">
                Action
              </th>
            </tr>
          </thead>
          <tbody>
            {Orders.map((item, index) => (
              <tr className="border-b dark:border-gray-700" key={index}>
                <th
                  scope="row"
                  className="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                >
                  #{item.id}
                </th>
                <td className="px-4 py-3">{item.address}</td>
                <td className="px-4 py-3">{item.phoneNumber}</td>
                <td className="px-4 py-3">
                  {item.totalPrice.toLocaleString("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  })}
                </td>
                <td className="px-4 py-3">{item.date}</td>
                <td className="px-4 py-3 flex items-center justify-start gap-4">
                  {item.status === true ? (
                    <div class="flex items-center">
                      <div class="h-2.5 w-2.5 rounded-full bg-green-500 me-2"></div>{" "}
                      Đã thanh toán
                    </div>
                  ) : (
                    <div class="flex items-center">
                      <div class="h-2.5 w-2.5 rounded-full bg-red-500 me-2"></div>{" "}
                      Chưa thanh toán
                    </div>
                  )}
                </td>
                <td>
                  <a
                    onClick={() => OnShowOrderDetail(item)}
                    className="font-medium text-blue-600 underline hover:cursor-pointer"
                  >
                    Xem chi tiết...
                  </a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <Pagination pagination={pagination} OnPageClick={OnPageClick} />
      </div>
    </div>
  );
}

export default OrderList;
