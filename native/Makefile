TARGET=linux
IPREFIX=-I/usr/include
LINKER_ARGS_linux=-L/usr/lib/x86_64-linux-gnu -lglib-2.0 -lgdk-3 -lgtk-3 -lgio-2.0 -lgobject-2.0

DEF_FILES := $(wildcard src/main/c_interop/*.def)

interop: $(DEF_FILES)

build/c_interop/%.klib: src/main/c_interop/%.def
	mkdir -p $(@D)
	cinterop -J-Xmx8g -copt ${IPREFIX}/atk-1.0 -compilerOpts ${IPREFIX}/gdk-pixbuf-2.0 -copt ${IPREFIX}/cairo \
			-copt ${IPREFIX}/pango-1.0 -copt -I/opt/local/lib/glib-2.0/include -copt \
			-I/usr/lib/x86_64-linux-gnu/glib-2.0/include -copt -I/usr/lib/glib-2.0/include \
	 		-copt ${IPREFIX}/gtk-3.0 -copt ${IPREFIX}/glib-2.0 -def $< \
			-target ${TARGET} -o $@
