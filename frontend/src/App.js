import { useState } from "react";
import Public from "./page/Public";
import { Routes, Route } from "react-router-dom";
import Home from "./component/public/Home";
import Product from "./component/public/Product";
import ProductDetail from "./component/public/ProductDetail";
import Cart from "./component/public/Cart";
import CheckOut from "./component/public/CheckOut";
import Admin from "./page/Admin";
import Dashboard from "./component/private/Dashboard";
import CategoryManagement from "./component/private/CategoryManagement";
import TransactionManagement from "./component/private/TransactionManagement";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ProductManagement from "./component/private/ProductManagement";
import PaymentResponse from "./component/public/PaymentResponse";
import { showSuccessDialog, showErrorDialog } from "./reuse";
import axios from "axios";

function App() {
  const initLoginState = {
    username: "",
    password: "",
  };
  const initRegisterState = {
    username: "",
    email: "",
    password: "",
  };
  const [login, setLogin] = useState(initLoginState);
  const [register, setRegister] = useState(initRegisterState);
  const [showLoginForm, setShowLoginForm] = useState(false);
  const [showRegisterForm, setShowRegisterForm] = useState(false);

  const initAuth = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : undefined;

  const [auth, setAuth] = useState(initAuth);

  const handleShowLoginForm = () => setShowLoginForm(true);
  const handleHideLoginForm = () => {
    setLogin(initLoginState);
    setShowLoginForm(false);
  };

  const handleShowRegisterForm = () => setShowRegisterForm(true);
  const handleHideRegisterForm = () => {
    setRegister(initRegisterState);
    setShowRegisterForm(false);
  };

  const handleChangeLogin = (e) => {
    setLogin((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };
  const handleChangeRegister = (e) => {
    setRegister((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleRegister = async (register) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/register",
        register
      );
      showSuccessDialog("register success");
    } catch (error) {
      console.log(error);
      showErrorDialog(error.response.data.message);
    }
    handleHideRegisterForm();
  };

  const handleLogin = async (login) => {
    try {
      const response = await axios.post(
        "http://localhost:8080/api/auth/login",
        login
      );
      localStorage.setItem("auth", JSON.stringify(response.data));
      setAuth(response.data);
      handleHideLoginForm();
      showSuccessDialog("Login success");
    } catch (error) {
      console.log(error);
      showErrorDialog("Thông tin đăng nhập không chính xác");
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("auth");
    setAuth(undefined);
    showSuccessDialog("Log out!!!");
  };

  console.log(register);
  return (
    <div className="">
      <Routes>
        <Route
          element={
            <Public
              LoginState={login}
              ShowLoginForm={showLoginForm}
              ShowRegisterForm={showRegisterForm}
              OnShowLoginForm={handleShowLoginForm}
              OnHideLoginForm={handleHideLoginForm}
              OnChangeLoginState={handleChangeLogin}
              OnLogin={() => handleLogin(login)}
              OnShowRegisterForm={handleShowRegisterForm}
              OnHideRegisterForm={handleHideRegisterForm}
              OnRegisterChange={handleChangeRegister}
              OnRegister={() => handleRegister(register)}
              OnLogout={handleLogout}
              Auth={auth}
              RegisterState={register}
            />
          }
          path="/"
        >
          <Route element={<Home />} exact path="/" />
          <Route element={<Product />} path="/product/:category_id" />
          <Route
            element={
              <ProductDetail
                Auth={auth}
                OnShowLoginForm={handleShowLoginForm}
                OnHideLoginForm={handleHideLoginForm}
              />
            }
            path="/productDetail/:product_id"
          />
          <Route element={<Cart />} path="/shoppingCart" />
          <Route element={<CheckOut />} path="/checkOut" />
        </Route>
        <Route element={<PaymentResponse />} path="/payment" />

        <Route path="/admin" element={<Admin OnLogout={handleLogout} />}>
          <Route path="/admin" element={<Dashboard />} />
          <Route path="/admin/category" element={<CategoryManagement />} />
          <Route path="/admin/product" element={<ProductManagement />} />
          <Route
            path="/admin/transaction"
            element={<TransactionManagement />}
          />
        </Route>
      </Routes>

      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
      {/* Same as */}
    </div>
  );
}

export default App;
