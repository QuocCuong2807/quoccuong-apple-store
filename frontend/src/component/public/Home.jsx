import axios from 'axios'
import React, { useEffect, useState } from 'react'
import ImageSlider from '../common/ImageSlider'
import ProductOverview from './ProductOverview'

function Home() {

  const [productOverviews, setProductOverviews] = useState([])


  useEffect(() => {
    getProductOverviews()
  },[])

  const getProductOverviews = async () => {
    const url = 'http://localhost:8080/api/product-overviews'
    try {
      const response = await axios.get(url)
      setProductOverviews(response.data)
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <div>
      <ImageSlider/>
      <div className='container px-8'>
        <ProductOverview props = {productOverviews}/>
      </div>
    </div>
  )
}

export default Home
