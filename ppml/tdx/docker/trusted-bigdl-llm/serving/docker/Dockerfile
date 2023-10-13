FROM intelanalytics/bigdl-llm-serving-cpu:2.4.0-SNAPSHOT

ARG http_proxy
ARG https_proxy

RUN mkdir -p /ppml/


ADD ./entrypoint.sh /opt/entrypoint.sh
ADD ./attestation.sh /opt/attestation.sh


# Install bigdl-ppml
RUN cd /root && \
    git clone https://github.com/intel-analytics/BigDL.git && \
    mv /root/BigDL/python/ppml/src/bigdl/ppml/ /usr/local/lib/python3.9/dist-packages/bigdl/ && \
    cp /usr/local/lib/python3.9/dist-packages/bigdl/ppml/attestation/attestation_cli.py /opt/ && \
    rm -rf /root/BigDL && \
    # Remote attestation dependencies
    mkdir -p /opt/intel/ && \
    cd /opt/intel && \
    wget https://download.01.org/intel-sgx/sgx-dcap/1.16/linux/distro/ubuntu20.04-server/sgx_linux_x64_sdk_2.19.100.3.bin && \
    chmod a+x ./sgx_linux_x64_sdk_2.19.100.3.bin && \
    printf "no\n/opt/intel\n"|./sgx_linux_x64_sdk_2.19.100.3.bin && \
    . /opt/intel/sgxsdk/environment && \
    cd /opt/intel && \
    wget https://download.01.org/intel-sgx/sgx-dcap/1.16/linux/distro/ubuntu20.04-server/sgx_debian_local_repo.tgz && \
    tar xzf sgx_debian_local_repo.tgz && \
    echo 'deb [trusted=yes arch=amd64] file:///opt/intel/sgx_debian_local_repo focal main' | tee /etc/apt/sources.list.d/intel-sgx.list && \
    wget -qO - https://download.01.org/intel-sgx/sgx_repo/ubuntu/intel-sgx-deb.key | apt-key add - && \
    apt-get update && \
    apt-get install -y libsgx-enclave-common-dev  libsgx-ae-qe3 libsgx-ae-qve libsgx-urts libsgx-dcap-ql libsgx-dcap-default-qpl libsgx-dcap-quote-verify-dev libsgx-dcap-ql-dev libsgx-dcap-default-qpl-dev libsgx-quote-ex-dev libsgx-uae-service libsgx-ra-network libsgx-ra-uefi libtdx-attest libtdx-attest-dev && \
    cd /ppml && \
    chmod +x /opt/entrypoint.sh && \
    chmod +x /opt/attestation.sh 


ENV PYTHONPATH /usr/lib/python3.9:/usr/lib/python3.9/lib-dynload:/usr/local/lib/python3.9/dist-packages:/usr/lib/python3/dist-packages

WORKDIR /ppml
ENTRYPOINT [ "/opt/entrypoint.sh" ]