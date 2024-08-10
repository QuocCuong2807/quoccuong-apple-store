import React from "react";
import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";

function StarRating({ rating }) {
  const stars = [];
  for (let i = 1; i <= 5; i++) {
    if (i <= rating) {
      stars.push(<FaStar color="yellow" />);
    } else if (i - rating < 1) {
      stars.push(<FaStarHalfAlt color="yellow" />);
    } else {
      stars.push(<FaRegStar color="yellow"/>);
    }
  }
  return <div className="flex mt-2 gap-2">{stars}</div>;
}

export default StarRating;
