import React from "react";
import { Slide } from "react-slideshow-image";
import "react-slideshow-image/dist/styles.css";

function ImageSlider() {
  const divStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    backgroundSize: "cover",
    height: "400px",
  };
  const spanStyle = {
    padding: "20px",
    background: "#efefef",
    color: "#000000",
  };

  const images = [
    "https://shopdunk.com/images/uploaded/banner/banner%202024/thang_6/banner%20watch%209%20T6_PC.jpg",
    "https://shopdunk.com/images/uploaded/banner/banner%202024/thang_6/banner%20MacBook%20Air%20M1%20T6_PC.jpg",
    "https://shopdunk.com/images/uploaded/banner/banner%202024/thang_6/banner%20iPad%20Air%205%20T6_PC.jpg",
  ];

  return (
    <div>
      <Slide>
        {images.map((slideImage, index) => (
          <div key={index}>
            <div style={{ ...divStyle, backgroundImage: `url(${slideImage})`, backgroundPosition:'center'}}>
              
            </div>
          </div>
        ))}
      </Slide>
    </div>
  );
}

export default ImageSlider;
