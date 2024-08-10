import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Loading from "../common/Loading";
import ProductSlider from "../common/ProductSlider";
import ProductInfo from "./ProductInfo";
import ProductRating from "./ProductRating";

function ProductDetail({ Auth, OnShowLoginForm }) {
  const { accessToken } = Auth ? Auth : "";
  const { product_id } = useParams();

  const [product, setProduct] = useState({});
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);

  const [rating, setRating] = useState([]);
  const [pagination, setPagination] = useState({});
  const [targetPage, setTargetPage] = useState(0);

  const [averageStar, setAverageStar] = useState(0.0);
  const [starOverview, setStarOverview] = useState([]);
  const [totalRating, setTotalRating] = useState(0);

  useEffect(() => {
    getProductById(product_id);
    getAverageStar(product_id);
    getStarOverview(product_id);
    getTotalRating(product_id);
  }, [product_id]);

  useEffect(() => {
    getAllRatingByProductId(product_id, targetPage);
  }, [targetPage]);

  const getTotalRating = async (productId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/rating/total-rating?productId=${productId}`
      );
      setTotalRating(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getStarOverview = async (productId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/rating/star-overview?productId=${productId}`
      );
      setStarOverview(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getAverageStar = async (productId) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/rating/average-star?productId=${productId}`
      );
      setAverageStar(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const getAllRatingByProductId = async (productId, targetPage) => {
    try {
      const requestUrl = `http://localhost:8080/api/rating/product-rating?productId=${productId}&p=${targetPage}`;
      const response = await fetch(requestUrl);
      const responseJson = await response.json();
      const { data } = responseJson;
      setRating(data);
      setPagination({
        isFirstPage: responseJson.firstPage,
        isLastPage: responseJson.lastPage,
        pageNo: responseJson.pageNumber,
        pageSize: responseJson.pageSize,
        totalPage: responseJson.totalPage,
      });
    } catch (error) {
      console.log(error);
    }
  };

  const getProductById = async (id) => {
    const url = `http://localhost:8080/api/product/${id}`;
    setLoading(true);
    try {
      const response = await axios.get(url);
      const { data } = response;
      setProduct(data);
      setImages(data.imageList);
    } catch (error) {
      console.log(error);
    }
    setLoading(false);
  };

  const handlePageClick = (e) => {
    setTargetPage(e.selected);
  };

  return (
    <>
      {loading ? (
        <Loading />
      ) : (
        <div className="px-40 py-10 bg-slate-50">
          <div className="grid grid-cols-12 gap-5">
            <div className="col-span-4">
              <ProductSlider props={images} />
            </div>
            <ProductInfo props={product} />
          </div>

          <ProductRating
            product={product}
            averageStar={averageStar}
            starOverview={starOverview}
            totalRating={totalRating}
            Token={accessToken}
            OnShowLoginForm={OnShowLoginForm}
            ratings={rating}
            pagination={pagination}
            OnPageClick={handlePageClick}
            GetRating={() => getAllRatingByProductId(product_id, targetPage)}
            GetAverageStar={() => getAverageStar(product_id)}
            GetTotalRating={() => getTotalRating(product_id)}
            GetStarOverview = {() => getStarOverview(product_id)}
          />
        </div>
      )}
    </>
  );
}

export default ProductDetail;
