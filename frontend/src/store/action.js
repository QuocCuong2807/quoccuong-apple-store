import {
  ADD_TO_CART,
  REMOVE_CART_ITEM,
  MINUS_CART_ITEM_QUANTITY,
  ENTER_ADDRESS,
  ENTER_PHONE_NUMBER,
  ENTER_FULL_NAME,
  ENTER_EMAIL,
  CHANGE_PAYMENT_METHOD
} from "./constant";

export const handleAddToCart = (payload) => ({
  type: ADD_TO_CART,
  payload,
});

export const handleRemoveCartItem = (payload) => ({
  type: REMOVE_CART_ITEM,
  payload,
});

export const handleMinusItemQuantity = (payload) => ({
  type: MINUS_CART_ITEM_QUANTITY,
  payload,
});

export const handleEnterFullName = (payload) => ({
  type: ENTER_FULL_NAME,
  payload,
});

export const handleEnterPhoneNumber = (payload) => {
  const value = payload;
  if (!validateNumber(value)) {
    payload = value.substring(0, value.length - 1);
  }

  return {
    type: ENTER_PHONE_NUMBER,
    payload,
  };
};

export const handleEnterAddress = (payload) => ({
  type: ENTER_ADDRESS,
  payload,
});
export const handleEnterEmail = (payload) => ({
  type: ENTER_EMAIL,
  payload,
});

export const handleChangePaymentMethod = (payload) => ({
  type: CHANGE_PAYMENT_METHOD, 
  payload
})

//validate type phone number
function validateNumber(input) {
  const regex = /^\d+$/;
  return regex.test(input);
}
