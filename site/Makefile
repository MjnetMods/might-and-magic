IMAGE=ghcr.io/gohugoio/hugo:v0.160.1

SITE=/site
PORT=1313

all: build

## Initialize Hugo module (run once, already done if go.mod exists)
init:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod init github.com/mjli/mam-site

## Download modules / themes
modules:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod tidy

## Update modules/themes to latest
update-modules:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod get -u

## Build static site
build:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	--minify

## Run development server (local Hugo — instant reload)
serve:
	hugo server \
		--bind 0.0.0.0 \
		--port $(PORT) \
		--baseURL http://localhost:$(PORT)/ \
		--disableFastRender \
		--buildDrafts \
		--buildFuture \
		--source .

## Run development server via Docker (no auto-reload on macOS)
docker-serve:
	docker run --rm \
	-p $(PORT):1313 \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	server \
	--bind 0.0.0.0 \
	--port 1313 \
	--baseURL http://localhost:1313/ \
	--disableFastRender \
	--buildDrafts \
	--buildFuture

## Clean generated files
clean:
	rm -rf public resources

help:
	@echo ""
	@echo "MAM Doc Site"
	@echo ""
	@echo "make modules      Download/update theme (run after clone or go.mod change)"
	@echo "make serve        Start dev server at localhost:1313 (requires local Hugo)"
	@echo "make build        Build static site via Docker"
	@echo ""
