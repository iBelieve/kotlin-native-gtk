IPREFIX := -I/usr/include
LINKER_ARGS := -L/usr/lib/x86_64-linux-gnu -L/usr/lib -lglib-2.0 -lgdk-3 -lgtk-3 -lgio-2.0 -lgobject-2.0

BIN_DIR := build/bin
BUILD_DIR := build
DEF_DIR := src/main/c_interop
LIB_DIR := src/main/kotlin
SAMPLE_DIR := src/sample/kotlin
GENERATED_DIR := $(BUILD_DIR)/generated/kotlin

all: sample

sample: $(BUILD_DIR) $(BUILD_DIR)/sample.kexe

gtk3: $(BUILD_DIR) $(GENERATED_DIR) $(BUILD_DIR)/gtk3.klib

interop: $(BUILD_DIR) $(BUILD_DIR)/gtk3.interop.klib

clean:
	rm -r build

$(BUILD_DIR):
	mkdir -p $@

$(GENERATED_DIR):
	mkdir -p $@

$(BUILD_DIR)/%.interop.klib: $(DEF_DIR)/%.def
	cinterop -J-Xmx8g \
			-copt ${IPREFIX}/glib-2.0 -copt -I/usr/lib/glib-2.0/include \
			-copt ${IPREFIX}/gtk-3.0 -copt ${IPREFIX}/pango-1.0 -copt ${IPREFIX}/gdk-pixbuf-2.0 \
			-copt ${IPREFIX}/atk-1.0 -copt ${IPREFIX}/cairo \
			-pkg $(notdir $(basename $@)) -def $< -o $(basename $@)

$(BUILD_DIR)/gtk3.klib: $(LIB_DIR) $(GENERATED_DIR) $(BUILD_DIR)/gtk3.interop.klib
	konanc $(LIB_DIR) $(GENERATED_DIR) -library $(BUILD_DIR)/gtk3.interop \
			-p library -o $(basename $@)

$(BUILD_DIR)/sample.kexe: $(SAMPLE_DIR) $(BUILD_DIR)/gtk3.klib $(BUILD_DIR)/gtk3.interop.klib
	konanc $(SAMPLE_DIR) -library $(BUILD_DIR)/gtk3 -library $(BUILD_DIR)/gtk3.interop \
			-linkerOpts "$(LINKER_ARGS)" -o $(basename $@)
