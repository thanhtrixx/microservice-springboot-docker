#!/usr/bin/env sh

echo "------------------------------------------------"
echo "                  Dockerize"
echo "------------------------------------------------"

echo "Dockerize SMS"
docker build -t sms -f Dockerfile-local-build sms

echo "Dockerize ThirdParty"
docker build -t third-party -f Dockerfile-local-build third-party

echo "Dockerize DataVoucher"
docker build -t data-voucher -f Dockerfile-local-build data-voucher

echo "Dockerize PurchaseData"
docker build -t purchase-data -f Dockerfile-local-build purchase-data

echo "------------------------------------------------"
echo "              Dockerize Success"
echo "------------------------------------------------"
