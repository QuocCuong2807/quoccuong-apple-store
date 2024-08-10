import {
  ADD_TO_CART,
  REMOVE_CART_ITEM,
  MINUS_CART_ITEM_QUANTITY,
  ENTER_ADDRESS,
  ENTER_PHONE_NUMBER,
  ENTER_FULL_NAME,
  ENTER_EMAIL,
  CHANGE_PAYMENT_METHOD,
  RESET_STATE,
} from "./constant";

//init state
const initState = {
  customerFullName: "",
  phoneNumber: "",
  email: "",
  address: "",
  cartItem: [],
  paymentMethod: "COD",
  cartItemQuantity: function () {
    return this.cartItem.length === 0
      ? 0
      : this.cartItem.reduce(
          (accumulator, currentValue) => accumulator + currentValue.quantity,
          0
        );
  },
  cartTotalPrice: function () {
    return this.cartItem.length === 0
      ? 0
      : this.cartItem.reduce(
          (accumulator, currentValue) =>
            accumulator + currentValue.totalPrice(),
          0
        );
  },
};

const findProductInCart = (state, payload) => {
  return state.cartItem.find((item) => item.id === payload.id);
};

//reducer
const reducer = (state = initState, action) => {
  const foundProduct = findProductInCart(state, action.payload);

  switch (action.type) {
    case ADD_TO_CART:
      if (foundProduct) {
        state.cartItem.forEach((item) => {
          if (item.id === foundProduct.id) item.quantity++;
        });
         return {
          ...state,
          cartItem: state.cartItem,
        };
      } else {
        return {
          ...state,
          cartItem: [
            ...state.cartItem,
            {
              ...action.payload,
              quantity: 1,
              totalPrice: function () {
                return this.quantity * this.price;
              },
            },
          ],
        };
      }
    case REMOVE_CART_ITEM:
      return {
        ...state,
        cartItem: state.cartItem.filter((item) => item.id !== foundProduct.id),
      };
    case MINUS_CART_ITEM_QUANTITY:
      if (action.payload.quantity > 1) {
        state.cartItem.forEach((item) => {
          if (item.id === action.payload.id) item.quantity--;
        });
        return {
          ...state,
          cartItem: state.cartItem,
        };
      } else {
        return {
          ...state,
          cartItem: state.cartItem.filter(
            (item) => item.id !== action.payload.id
          ),
        };
      }
    case ENTER_FULL_NAME:
      return {
        ...state,
        customerFullName: action.payload,
      };
    case ENTER_PHONE_NUMBER:
      return { ...state, phoneNumber: action.payload };
    case ENTER_ADDRESS:
      return {
        ...state,
        address: action.payload,
      };
    case ENTER_EMAIL:
      return {
        ...state,
        email: action.payload,
      };
    case CHANGE_PAYMENT_METHOD:
      return {
        ...state,
        paymentMethod: action.payload,
      };
    case RESET_STATE:
      return {
        ...initState,
      };
    default:
      return { ...state };
  }
};

export default reducer;
