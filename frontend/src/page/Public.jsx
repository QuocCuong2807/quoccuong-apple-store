import axios from "axios";
import React, { useEffect, useState } from "react";
import { Outlet } from "react-router-dom";
import Login from "../component/common/Login";
import Modal from "../component/common/Modal";
import PublicHeader from "../component/common/PublicHeader";
import Register from "../component/common/Register";
import { Provider } from "react-redux";

import store from "../store/store";

function Public({
  LoginState,
  ShowLoginForm,
  ShowRegisterForm,
  OnShowLoginForm,
  OnHideLoginForm,
  OnChangeLoginState,
  OnLogin,
  OnShowRegisterForm,
  OnHideRegisterForm,
  OnRegisterChange,
  OnRegister,
  OnLogout,
  Auth,
  RegisterState,
}) {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    getAllProductCategories();
  }, []);

  const getAllProductCategories = async () => {
    const url = "http://localhost:8080/api/v2/categories";
    try {
      const response = await axios.get(url);
      setCategories(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="bg-slate-100">
      <Provider store={store}>
        <PublicHeader
          props={categories}
          OnShowLoginForm={() => OnShowLoginForm()}
          OnShowRegisterForm={() => OnShowRegisterForm()}
          OnLogout={OnLogout}
          Auth={Auth}
        />

        <Outlet />

        <Modal show={ShowLoginForm} OnHideModal={OnHideLoginForm}>
          <Login props = {LoginState} OnAuthChange={OnChangeLoginState} OnLoginSubmit={OnLogin} />
        </Modal>

        <Modal show={ShowRegisterForm} OnHideModal={OnHideRegisterForm}>
          <Register
            props={RegisterState}
            OnRegisterChange={OnRegisterChange}
            OnRegisterSubmit={OnRegister}
          />
        </Modal>
      </Provider>
    </div>
  );
}

export default Public;
