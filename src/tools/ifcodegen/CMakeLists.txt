qt_path_join(templates_install_dir "${QT_INSTALL_DIR}"
             "${INSTALL_DATADIR}/ifcodegen-templates")

qt_copy_or_install(
  FILES
  templates/aidl/struct.aidl.j2
  templates/aidl/plugin.h.j2
  templates/aidl/callback.h.j2
  templates/aidl/callback.cpp.j2
  templates/aidl/CMakeLists.txt.j2
  templates/aidl/callback.aidl.j2
  templates/aidl/enum.aidl.j2
  templates/aidl/service.aidl.j2
  templates/aidl/plugin.cpp.j2
  templates/aidl/backend.h.j2
  templates/aidl/plugin.json.j2
  templates/aidl/backend.cpp.j2
  templates/aidl/filters.py
  DESTINATION
  "${templates_install_dir}/aidl")

qt_copy_or_install(FILES templates/aidl.yaml DESTINATION
                   "${templates_install_dir}")
