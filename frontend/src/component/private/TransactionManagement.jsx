import axios from "axios";
import React, { useEffect, useState } from "react";
import Modal from "../common/Modal";
import OrderDetail from "./OrderDetail";
import OrderList from "./OrderList";

function TransactionManagement() {
  const [showOrderDetail, setShowOrderDetail] = useState(false);
  const [filter, setFilter] = useState("ALL");
  const [targetPage, setTargetPage] = useState(0);
  const [pagination, setPagination] = useState({});
  const [orders, setOrders] = useState([]);
  const [orderDetail, setOrderDetail] = useState({});

 
  const auth = JSON.parse(localStorage.getItem("auth"));
  const { accessToken } = auth;
  useEffect(() => {
    getAllOrders(targetPage, filter);
  }, [targetPage, filter]);

  const handleShowOrderDetail = (item) => {
    setOrderDetail(item);
    setShowOrderDetail(true);
  };
  const handleHideOrderDetail = () => {
    setOrderDetail({});
    setShowOrderDetail(false);
  };

  const handlePageClick = (e) => {
    setTargetPage(e.selected);
  };
  const handleChangeFilter = (e) => {
    setFilter(e.target.value);
  };

  const getAllOrders = async (targetPage, filter) => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/order/orders?p=${targetPage}&statusFilter=${filter}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      const responseJson = await response.json();
      const { data } = responseJson;
      console.log(data);
      setOrders(data);
      setPagination({
        isFirstPage: responseJson.firstPage,
        isLastPage: responseJson.lastPage,
        pageNo: responseJson.pageNumber,
        pageSize: responseJson.pageSize,
        totalPage: responseJson.totalPage,
      });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      <h1 className="bg-gray-50 font-medium font-mono text-xl px-16 pt-6">
        Quản lý giao dịch
      </h1>
      <section className="bg-gray-50 dark:bg-gray-900 p-3 sm:p-5">
        <div className="mx-auto max-w-screen-xl px-4 lg:px-12">
          <div className="bg-white dark:bg-gray-800 relative shadow-md sm:rounded-lg overflow-hidden">
            <OrderList
              OnShowOrderDetail={handleShowOrderDetail}
              OnFilterChange={handleChangeFilter}
              Orders={orders}
              pagination={pagination}
              OnPageClick={handlePageClick}
              Token = {accessToken}
            />
          </div>
        </div>
      </section>
      <Modal show={showOrderDetail} OnHideModal={handleHideOrderDetail}>
        <OrderDetail props={orderDetail} />
      </Modal>
    </div>
  );
}

export default TransactionManagement;
