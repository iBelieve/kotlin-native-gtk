IPREFIX := -I/usr/include
LINKER_ARGS_linux := -L/usr/lib/x86_64-linux-gnu -lglib-2.0 -lgdk-3 -lgtk-3 -lgio-2.0 -lgobject-2.0

BUILD_DIR := build/c_interop
DEF_DIR := src/main/c_interop

interop: $(BUILD_DIR) $(BUILD_DIR)/gtk3.klib

clean:
	rm -r build

$(BUILD_DIR):
	mkdir -p $@

$(BUILD_DIR)/%.klib: $(DEF_DIR)/%.def
	cinterop -J-Xmx8g \
			-copt ${IPREFIX}/glib-2.0 -copt -I/usr/lib/glib-2.0/include \
			-copt ${IPREFIX}/gtk-3.0 -copt ${IPREFIX}/pango-1.0 -copt ${IPREFIX}/gdk-pixbuf-2.0 \
			-copt ${IPREFIX}/atk-1.0 -copt ${IPREFIX}/cairo \
			-pkg $(notdir $(basename $@)).interop -def $< -o $(basename $@).interop
