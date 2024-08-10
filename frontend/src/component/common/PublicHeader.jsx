import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { MdLogout } from "react-icons/md";
import { RiAdminLine } from "react-icons/ri";
import ProductSearchBar from "./ProductSearchBar";
import ProductSearchResult from "./ProductSearchResult";
import { useDebounce } from "../../hooks";
import axios from "axios";

function PublicHeader({
  props,
  OnShowLoginForm,
  OnShowRegisterForm,
  OnLogout,
  Auth,
}) {
  const cartItemQuantity = useSelector((item) => item.cartItemQuantity());
  const { accessToken, role } = Auth ? Auth : {};
  const [loading, setLoading] = useState(false);
  const [searchName, setSearchName] = useState("");
  const [products, setProducts] = useState([]);

  const debouncedSearch = useDebounce(searchName);

  const initAuth = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : {};

  useEffect(() => {
    getProductsByName(debouncedSearch);
  }, [debouncedSearch]);

  const getProductsByName = async (productName) => {
    setLoading(true);
    try {
      const response = await axios.get(
        `http://localhost:8080/api/v3/products?name=${productName}`
      );

      setProducts(response.data);
    } catch (error) {
      console.log(error);
    }
    setLoading(false);
  };

  const handleChangeSearchName = (e) => {
    setSearchName(e.target.value);
  };
  const handleSetDefaultSearchName = () => {
    setSearchName("");
  };

  return (
    <div className="flex justify-center items-center gap-x-6 text-lg bg-white p-4">
      <ul id="product-category" className="flex gap-x-6 text-base">
        {props.map((item, index) => (
          <li key={index}>
            <Link to={`/product/${item.id}`}>{item.name}</Link>
          </li>
        ))}
      </ul>

      <div
        id="logo"
        className="font-mono font-bold mx-6 text-xl hover:cursor-pointer"
      >
        <Link to="/">
          <h3>MyShop</h3>
        </Link>
      </div>

      <ul id="users-features" className="flex gap-x-6 items-center text-base">
        {accessToken ? (
          role.includes("ADMIN") ? (
            <Link to="/admin">
              <div className="flex items-center hover:cursor-pointer">
                <RiAdminLine />
                <p className="ml-2">Admin</p>
              </div>
            </Link>
          ) : (
            <div
              className="flex items-center hover:cursor-pointer"
              onClick={() => OnLogout()}
            >
              <MdLogout />
              <p className="ml-2">Log out</p>
            </div>
          )
        ) : (
          <>
            <li className="hover:cursor-pointer">
              <a onClick={OnShowLoginForm}>Đăng nhập</a>
            </li>
            <li className="hover:cursor-pointer">
              <a onClick={OnShowRegisterForm}>Đăng ký</a>
            </li>
          </>
        )}

        <li>
          <Link to="/shoppingCart" className="relative">
            <svg
              className="w-6"
              data-slot="icon"
              fill="none"
              stroke-width="1.5"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M2.25 3h1.386c.51 0 .955.343 1.087.835l.383 1.437M7.5 14.25a3 3 0 0 0-3 3h15.75m-12.75-3h11.218c1.121-2.3 2.1-4.684 2.924-7.138a60.114 60.114 0 0 0-16.536-1.84M7.5 14.25 5.106 5.272M6 20.25a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Zm12.75 0a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z"
              ></path>
            </svg>
            <span className="absolute bg-red-400 bottom-3 left-1/2 rounded-full w-6 h-6 flex justify-center items-start text-white">
              {cartItemQuantity}
            </span>
          </Link>
        </li>
        <li>
          <ProductSearchBar
            searchValue={searchName}
            OnSearchNameChange={handleChangeSearchName}
            OnResetSearchName={handleSetDefaultSearchName}
          />
          {loading ? (
            <div>Loading...</div>
          ) : products.length > 0 ? (
            <ProductSearchResult props={products} />
          ) : (
            <></>
          )}
        </li>
      </ul>
    </div>
  );
}

export default PublicHeader;
