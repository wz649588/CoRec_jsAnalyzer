FROM ubuntu:17.10

RUN groupadd -r analysis && useradd -m --no-log-init --gid analysis analysis

USER analysis
COPY src /analyzer

WORKDIR /
CMD ["/analyzer/analyze.sh"]