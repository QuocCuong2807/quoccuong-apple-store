import React from "react";
import CartItems from "./CartItems";
import OrderSummary from "./OrderSummary";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";

function Cart() {
  const cart = useSelector((item) => item);
  console.log(cart);
  return cart.cartItem.length > 0 ? (
    <div className="grid grid-cols-3 gap-4 py-6">
      <CartItems cartItem={cart.cartItem} />
      <OrderSummary totalPrice={cart.cartTotalPrice()} />
    </div>
  ) : (
    <div>
      <h1 className="font-mono text-3xl text-center bg-white py-5">
        Chưa có sản phẩm nào trong giỏ hàng
      </h1>
      <div className="text-center bg-white text-blue-400 underline">
        <Link to="/"> Tiếp tục mua hàng</Link>
      </div>
    </div>
  );
}

export default Cart;
