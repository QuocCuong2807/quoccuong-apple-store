import React, { useState } from "react";
import { FaStar } from "react-icons/fa";
import { CiClock1 } from "react-icons/ci";
import Pagination from "../common/Pagination";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { showSuccessDialog } from "../../reuse";
import Loading from "../common/Loading";
import StarRating from "../common/StarRating";

function ProductRating({
  product,
  averageStar,
  starOverview,
  totalRating,
  Token,
  OnShowLoginForm,
  ratings,
  pagination,
  OnPageClick,
  GetRating,
  GetAverageStar,
  GetTotalRating,
  GetStarOverview,
}) {
  const [loading, setLoading] = useState(false);
  const [star, setStar] = useState(0);
  const [hoverStar, setHoverStar] = useState(0);
  const [ratingContent, setRatingContent] = useState("");
  const userName = Token ? jwtDecode(Token).sub : "";
  const handleRatingContentChange = (e) => {
    setRatingContent(e.target.value);
  };

  const handleSubmitRating = async () => {
    setLoading(true);
    const url = "http://localhost:8080/api/rating/new-rating";
    const rating = {
      userName,
      star,
      ratingContent,
      productId: product.id,
      date: Date.now(),
    };
    console.log(rating);
    try {
      const response = await axios.post(url, rating, {
        headers: { Authorization: `Bearer ${Token}` },
      });
      showSuccessDialog("Đánh giá thành công");
      setStar(0);
      setRatingContent("");
      GetRating();
      GetAverageStar();
      GetTotalRating();
      GetStarOverview();
    } catch (error) {
      console.log(error);
    }
    setLoading(false);
  };

  return loading ? (
    <Loading />
  ) : (
    <div className="mt-16 bg-white p-8 rounded-lg">
      <h1 className="font-bold text-lg">Đánh giá & nhận xét {product.name}</h1>
      <div className="p-4 flex items-center">
        <div className="border-r border-black py-6 pr-10">
          <h1 className="font-bold text-3xl ml-5">{averageStar}/5</h1>
          <StarRating rating={averageStar} />
          <h1 className="ml-3 mt-2">{totalRating} đánh giá</h1>
        </div>
        <div className="ml-6">
          {starOverview.map((item, index) => (
            <div key={index} className="flex items-center mt-2">
              <p className="font-medium mr-2">{item.label}</p>
              <FaStar color="yellow" />
              <p className="ml-2">{item.quantity} đánh giá</p>
            </div>
          ))}
        </div>
      </div>

      <div className="border my-4"></div>

      {Token ? (
        <div>
          <h1 className="font-bold text-lg text-center">
            Bạn đánh giá sao về sản phẩm này
          </h1>
          <form>
            <label
              for="message"
              className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
            >
              Đánh giá của bạn
            </label>
            <textarea
              id="message"
              rows="2"
              className="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="Thêm đánh giá ở đây..."
              onChange={(e) => handleRatingContentChange(e)}
              name="content"
              value={ratingContent}
            />
            <div className="flex justify-center my-4">
              {[...Array(5)].map((crrStar, index) => {
                const currentStar = index + 1;
                return (
                  <label>
                    <input
                      className="hidden"
                      type="radio"
                      name="rating"
                      value={currentStar}
                      onClick={() => setStar(currentStar)}
                    />
                    <FaStar
                      size={25}
                      className="cursor-pointer mx-1"
                      color={
                        currentStar <= (hoverStar || star)
                          ? "yellow"
                          : "#e5e5e9"
                      }
                      onMouseEnter={() => setHoverStar(currentStar)}
                      onMouseLeave={() => setHoverStar(0)}
                    />
                  </label>
                );
              })}
            </div>
            <div className="flex justify-center">
              <button
                type="button"
                className="text-white bg-gradient-to-r from-blue-500 via-blue-600 to-blue-700 hover:bg-gradient-to-br focus:ring-4 focus:outline-none focus:ring-blue-300 dark:focus:ring-blue-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2"
                onClick={() => handleSubmitRating()}
              >
                Thêm đánh giá
              </button>
            </div>
          </form>
        </div>
      ) : (
        <div>
          <h1 className="text-center">Vui lòng đăng nhập trước khi đánh giá</h1>
          <div className="flex justify-center mt-4">
            <button
              type="submit"
              className="mx-auto text-white bg-teal-500 hover:bg-teal-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-primary-600 dark:hover:bg-primary-700 dark:focus:ring-primary-800"
              onClick={OnShowLoginForm}
            >
              Đăng nhập ngay
            </button>
          </div>
        </div>
      )}

      <div className="border my-4"></div>

      <div className="mt-2">
        <h1 className="font-bold text-lg">Các đánh giá khác</h1>
        <div className="mt-4">
          <ul>
            {ratings.length !== 0 ? (
              ratings.map((item, index) => (
                <li className="ml-2 mt-2 rounded-xl">
                  <div className="border p-4">
                    <div className="flex items-center gap-4">
                      <p className="font-medium">{item.userName}</p>
                      <div className="flex items-center">
                        <CiClock1 />
                        <p>{item.date}</p>
                      </div>
                    </div>
                    <div className="flex gap-1 mt-1">
                      {[...Array(item.star)].map((item, index) => (
                        <FaStar color="yellow" key={index} />
                      ))}
                    </div>
                    <div className="mt-2 ml-2 ">
                      <p> {item.ratingContent}</p>
                    </div>
                  </div>
                </li>
              ))
            ) : (
              <h1 className="font-mono italic text-center text-lg text-slate-500">
                Hiện chưa có đánh giá nào cho sản phẩm này...
              </h1>
            )}
          </ul>
        </div>
        <Pagination pagination={pagination} OnPageClick={OnPageClick} />
      </div>
    </div>
  );
}

export default ProductRating;
