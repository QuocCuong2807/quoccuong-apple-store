import axios from "axios";
import React, { useEffect, useState } from "react";
import store from "../../store/store";
import Loading from "../common/Loading";
import PaymentSuccess from "./PaymentSuccess";
import { useNavigate } from "react-router-dom";
import { showSuccessDialog } from "../../reuse";


function PaymentResponse() {
  const [loading, setLoading] = useState(true);
  const [isPaymentSuccess, setPaymentSuccess] = useState(false);

  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  const paramValue = urlParams.get("vnp_ResponseCode");
  const cart = JSON.parse(localStorage.getItem("cart"));
  const province = localStorage.getItem("province");
  const district = localStorage.getItem("district");
  const ward = localStorage.getItem("ward");

  const navigate = useNavigate()

  useEffect(() => {
    handleVNPPayment(cart, province, district, ward, paramValue);
  }, []);

  const handleVNPPayment = async (
    cart,
    province,
    district,
    ward,
    vnp_ResponseCode
  ) => {
    try {
      const url = `http://localhost:8080/api/payment/vnp-payment?province=${province}&district=${district}&ward=${ward}&vnp_ResponseCode=${vnp_ResponseCode}`;
      const response = await axios.post(url, cart);
      localStorage.removeItem("province");
      localStorage.removeItem("district");
      localStorage.removeItem("ward");
      localStorage.removeItem("cart");
      setLoading(false);
      showSuccessDialog("Thanh toán thành công")
      navigate("/")
    } catch (error) {
      localStorage.removeItem("province");
      localStorage.removeItem("district");
      localStorage.removeItem("ward");
      localStorage.removeItem("cart");
      setLoading(false);
      showSuccessDialog("Thanh toán thất bại, có lỗi")
      navigate("/")
    }
  };

  return loading ? <Loading /> : <div></div>;
}

export default PaymentResponse;
