#!/bin/bash
for i in {1..10}
do
   curl -s http://localhost:8080/api/v1/products/1 &
done

wait
echo "All requests done"
