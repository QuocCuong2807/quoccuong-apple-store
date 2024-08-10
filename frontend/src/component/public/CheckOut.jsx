import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import store from "../../store/store";
import { showSuccessDialog, showErrorDialog } from "../../reuse";
import {
  handleChangePaymentMethod,
  handleEnterAddress,
  handleEnterEmail,
  handleEnterFullName,
  handleEnterPhoneNumber,
} from "../../store/action";
import axios from "axios";
import { RESET_STATE } from "../../store/constant";
import Loading from "../common/Loading";


function CheckOut() {
  const cart = useSelector((item) => item);
  const totalPrice = useSelector((item) => item.cartTotalPrice());

  const [provinces, setProvinces] = useState([]);
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);

  const [province, setProvince] = useState("");
  const [district, setDistrict] = useState("");
  const [ward, setWard] = useState("");

  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    getAllProvinces();
  }, []);

  const getAllProvinces = async () => {
    try {
      const response = await fetch("https://esgoo.net/api-tinhthanh/1/0.htm");
      const data = await response.json();
      console.log(data.data);
      setProvinces(data.data);
      setWards([]);
    } catch (error) {
      console.log(error);
    }
  };
  const getAllDistrict = async (id) => {
    try {
      const response = await fetch(
        `https://esgoo.net/api-tinhthanh/2/${id}.htm`
      );
      const data = await response.json();
      setDistricts(data.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getAllWards = async (id) => {
    try {
      const response = await fetch(
        `https://esgoo.net/api-tinhthanh/3/${id}.htm`
      );
      const data = await response.json();
      setWards(data.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getSpecificProvinceDistrictWards = async (id) => {
    try {
      const response = await fetch(
        `https://esgoo.net/api-tinhthanh/5/${id}.htm`
      );
      const data = await response.json();
      return data.data.name;
    } catch (error) {
      console.log(error);
    }
  };

  const handleChooseProvinceChangeDistrict = async (id) => {
    try {
      const name = await getSpecificProvinceDistrictWards(id);
      setProvince(name);
      getAllDistrict(id);
      setDistrict("");
    } catch (error) {
      console.log(error);
    }
  };

  const handleChooseDistrictChangeWards = async (id) => {
    try {
      getAllWards(id);
      const name = await getSpecificProvinceDistrictWards(id);
      const arrName = name.split(",");
      setDistrict(arrName[arrName.length - 2]);
      setWard("");
    } catch (error) {
      console.log(error);
    }
  };

  const handleChooseWard = async (id) => {
    try {
      const name = await getSpecificProvinceDistrictWards(id);
      const arrName = name.split(",");
      setWard(arrName[arrName.length - 3]);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCheckOut = async (cart, totalPrice, province, district, ward) => {
    setLoading(true);
    try {
      if (cart.paymentMethod === "COD") {
        const url = `http://localhost:8080/api/payment/cod-payment?&province=${province}&district=${district}&ward=${ward}`;
        const response = await axios.post(url, cart);
        store.dispatch({ type: RESET_STATE, payload: cart });
        setLoading(false);
        showSuccessDialog(response.data);
        navigate("/");
      } else {
        const url = `http://localhost:8080/api/payment/vnp-order-creation?amount=${totalPrice}&txt_billing_mobile=${cart.phoneNumber}&txt_billing_email=${cart.email}&bill_fullName=${cart.customerFullName}&province=${province}&district=${district}&ward=${ward}&address=${cart.address}`;

        //store cart, address to local storage
        localStorage.setItem("province", province);
        localStorage.setItem("district", district);
        localStorage.setItem("ward", ward);
        localStorage.setItem("cart", JSON.stringify(cart));
        const response = await axios.get(url);

        //reset cart and redirect to vnp payment page
        store.dispatch({ type: RESET_STATE, payload: cart });
        setLoading(false);
        window.location.href = response.data.data;
      }
    } catch (error) {
      console.log(error);
    }
  };

  return loading ? (
    <Loading />
  ) : (
    <div className="my-4 px-10">
      <h1 className="font-medium text-2xl my-6 text-center">Check Out</h1>
      <form className="bg-white  rounded-lg p-10 mx-20">
        <h1 className="font-medium text-2xl py-4">Thông tin giao hàng</h1>
        <div className="grid grid-cols-3 gap-y-10">
          <div>
            <label
              for="your_name"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              Họ tên:
            </label>
            <input
              type="text"
              id="your_name"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              placeholder="Enter your name..."
              required
              onChange={(e) =>
                store.dispatch(handleEnterFullName(e.target.value))
              }
              value={cart.customerFullName}
            />
          </div>

          <div>
            <label
              for="your_email"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              Email:
            </label>
            <input
              type="email"
              id="your_email"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              placeholder="name@flowbite.com"
              required
              onChange={(e) => store.dispatch(handleEnterEmail(e.target.value))}
              value={cart.email}
            />
          </div>

          <div>
            <label
              for="your_email"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              {" "}
              Số điện thoại
            </label>
            <input
              type="number"
              id="phoneNumber"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              placeholder="(+84)...."
              required
              onChange={(e) =>
                store.dispatch(handleEnterPhoneNumber(e.target.value))
              }
              value={cart.phoneNumber}
            />
          </div>

          <div>
            <div className="mb-2 flex items-center gap-2">
              <label
                for="select-country-input-3"
                className="block text-sm font-medium text-gray-900 dark:text-white"
              >
                {" "}
                Thành phố
              </label>
            </div>
            <select
              id="select-country-input-3"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              onChange={(e) =>
                handleChooseProvinceChangeDistrict(e.target.value)
              }
            >
              <option selected>Chọn Tỉnh/tp</option>
              {provinces.map((item, index) => (
                <option key={index} value={item.id}>
                  {item.name}
                </option>
              ))}
            </select>
          </div>

          <div>
            <div className="mb-2 flex items-center gap-2">
              <label
                for="select-city-input-3"
                className="block text-sm font-medium text-gray-900 dark:text-white"
              >
                Quận/huyện
              </label>
            </div>
            <select
              id="select-city-input-3"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              onChange={(e) =>
                handleChooseDistrictChangeWards(e.target.value, e.target.name)
              }
            >
              <option selected>Chọn Quận/huyện</option>
              {districts.map((item, index) => (
                <option key={index} value={item.id}>
                  {item.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <div className="mb-2 flex items-center gap-2">
              <label
                for="select-city-input-3"
                className="block text-sm font-medium text-gray-900 dark:text-white"
              >
                Phường/xã
              </label>
            </div>
            <select
              id="select-city-input-3"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              onChange={(e) => handleChooseWard(e.target.value)}
            >
              <option selected>Chọn phường/xã</option>
              {wards.map((item, index) => (
                <option key={index} value={item.id}>
                  {item.name}
                </option>
              ))}
            </select>
          </div>
          <div>
            <label
              for="your_name"
              className="mb-2 block text-sm font-medium text-gray-900 dark:text-white"
            >
              {" "}
              Địa chỉ
            </label>
            <input
              type="text"
              id="your_name"
              className="block w-2/3 rounded-lg border border-gray-300 bg-gray-50 p-2.5 text-sm text-gray-900 focus:border-primary-500 focus:ring-primary-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder:text-gray-400 dark:focus:border-primary-500 dark:focus:ring-primary-500"
              placeholder="..."
              required
              onChange={(e) =>
                store.dispatch(handleEnterAddress(e.target.value))
              }
              value={cart.address}
            />
          </div>
        </div>

        <div className="border my-6"></div>

        <h1 className="font-medium text-2xl mb-2">Phương thức thanh toán</h1>

        <div className="flex gap-4  items-center">
          <div className="bg-slate-100 flex justify-center rounded-lg border p-4 items-center gap-4">
            <input
              type="radio"
              value="COD"
              onChange={(e) =>
                store.dispatch(handleChangePaymentMethod(e.target.value))
              }
              checked={cart.paymentMethod === "COD"}
            />
            <div>
              <img
                className="w-6"
                src="data:image/jpeg;base64,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URAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQEREBERAREQf/2Q=="
              />
            </div>
            <p>Ship Cod</p>
          </div>
          <div className="bg-slate-100 flex justify-center rounded-lg border p-4 items-center gap-4">
            <input
              type="radio"
              value="VNPAY"
              onChange={(e) =>
                store.dispatch(handleChangePaymentMethod(e.target.value))
              }
              checked={cart.paymentMethod === "VNPAY"}
            />
            <div>
              <img
                className="w-6"
                src="data:image/png;base64,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"
              />
            </div>
            <p>VNPay</p>
          </div>
        </div>

        <div className="border my-6"></div>
        <button
          type="button"
          className="text-white bg-gradient-to-br from-green-400 to-blue-600 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-green-200 dark:focus:ring-green-800 font-medium rounded-lg text-sm px-12 py-4 text-center me-2 mb-2"
          onClick={() =>
            handleCheckOut(cart, totalPrice, province, district, ward)
          }
        >
          Đặt hàng
        </button>
      </form>
    </div>
  );
}

export default CheckOut;
