#!/usr/bin/env bash

BASE_URL="http://localhost:8080"
ADMIN_TOKEN="YOUR_ADMIN_JWT"

add_image () {
  local product_id="$1"
  local image_text="$2"

  curl -i -X POST "$BASE_URL/products/$product_id/images" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -d "{
      \"imageUrl\": \"https://placehold.co/800x600/png?text=$image_text\",
      \"primaryImage\": true
    }"
  echo
}

add_image 1 "iPhone+15+Pro"
add_image 2 "MacBook+Air+M3"
add_image 3 "Sony+WH-1000XM5"
add_image 4 "Mens+Casual+Shirt"
add_image 5 "Womens+Running+Shoes"
add_image 6 "Air+Fryer"
add_image 7 "Microwave+Oven"
add_image 8 "Atomic+Habits"
add_image 9 "The+Alchemist"
add_image 10 "Face+Wash"
add_image 11 "Yoga+Mat"
add_image 12 "Protein+Bars"
add_image 13 "Board+Game"