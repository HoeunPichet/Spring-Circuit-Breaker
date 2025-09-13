#!/bin/bash
for i in {1..10}
do
   curl -s http://localhost:8080/api/v1/products/1 &
#   sleep 1   # 1 second delay between starting requests
done

wait
echo "All requests done"
