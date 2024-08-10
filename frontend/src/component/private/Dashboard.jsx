import React, { useEffect, useState } from "react";
import IncomeAnalyze from "./IncomeAnalyze";
import TotalOverview from "./TotalOverview";
import axios from "axios";

function Dashboard() {
  const [revenue, setRevenue] = useState(0);
  const [productQuantity, setProductQuantity] = useState(0);
  const [orderQuantity, setOrderQuantity] = useState(0);
  const [dataChart, setDataChart] = useState([]);
  const [revenueFilter, setRevenueFilter] = useState("TOTAL_REVENUE");
  
  const auth = JSON.parse(localStorage.getItem("auth"));
  const { accessToken } = auth;


  useEffect(() => {
    getTotalRevenue();
    getTotalOrderQuantity();
    getTotalProductQuantity();
    getRevenueByProductCategory(revenueFilter);
  }, []);

  useEffect(() => {
    getRevenueByProductCategory(revenueFilter);
  }, [revenueFilter]);

  const getRevenueByProductCategory = async (revenueFilter) => {
    const url = `http://localhost:8080/api/dashboard/total-incomes-analysis?filter=${revenueFilter}`;
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setDataChart(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getTotalRevenue = async () => {
    const url = "http://localhost:8080/api/dashboard/revenue";
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setRevenue(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  const getTotalProductQuantity = async () => {
    const url = "http://localhost:8080/api/dashboard/product-quantity";
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setProductQuantity(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  const getTotalOrderQuantity = async () => {
    const url = "http://localhost:8080/api/dashboard/order-quantity";
    try {
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      setOrderQuantity(response.data);
    } catch (error) {
      console.log(error);
    }
  };
  const handleChangeRevenueFilter = (e) => {
    setRevenueFilter(e.target.value);
  };

  return (
    <div>
      <TotalOverview
        Revenue={revenue}
        ProductQuantity={productQuantity}
        OrderQuantity={orderQuantity}
      />
      <IncomeAnalyze
        OnRevenueFilterChange={handleChangeRevenueFilter}
        data={dataChart}
      />
    </div>
  );
}

export default Dashboard;
