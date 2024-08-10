import React from "react";
import { Slide } from "react-slideshow-image";
import "react-slideshow-image/dist/styles.css";
function ProductSlider({props}) {
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
    "https://shopdunk.com/images/thumbs/0020075_iphone-15-pro-max-128gb_550.jpeg",
    "https://shopdunk.com/images/thumbs/0020075_iphone-15-pro-max-128gb_550.jpeg",
    "https://shopdunk.com/images/thumbs/0020078_iphone-15-pro-max-128gb_550.jpeg",
  ];
  console.log(props)
  return (
    <div>
      <Slide>
        {props.map((slideImage, index) => (
          <div key={index}>
            <div
              style={{
                ...divStyle,
                backgroundImage: `url(${slideImage})`,
                backgroundPosition: "center",
              }}
            ></div>
          </div>
        ))}
      </Slide>
    </div>
  );
}

export default ProductSlider;
