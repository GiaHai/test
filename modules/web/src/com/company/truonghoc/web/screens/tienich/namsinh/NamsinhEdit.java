package com.company.truonghoc.web.screens.tienich.namsinh;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.tienich.Namsinh;

import javax.inject.Inject;
import java.util.List;

@UiController("truonghoc_Namsinh.edit")
@UiDescriptor("namsinh-edit.xml")
@EditedEntityContainer("namsinhDc")
@LoadDataBeforeShow
public class NamsinhEdit extends StandardEditor<Namsinh> {
    @Inject
    protected DataContext dataContext;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TextField<String> namSinhField;
    @Inject
    protected Notifications notifications;

    @Subscribe("commitAndCloseBtn")
    protected void onCommitAndCloseBtnClick(Button.ClickEvent event) {
        if (loadNamsinh().size() == 0){
            commitChanges().then(() -> {
                dataContext.clear();

                Namsinh namsinh = dataContext.create(Namsinh.class);
                getEditedEntityContainer().setItem(namsinh);
            });
        }else {
            notifications.create()
                    .withCaption("Bạn đã nhập Năm này rồi !")
                    .withPosition(Notifications.Position.BOTTOM_RIGHT)
                    .withType(Notifications.NotificationType.WARNING)
                    .show();

            namSinhField.clear();
        }

    }

    private List<Namsinh> loadNamsinh(){
        return dataManager.load(Namsinh.class)
                .query("select e from truonghoc_Namsinh e where e.namSinh = :namsinh")
                .parameter("namsinh", namSinhField.getValue())
                .list();
    }
}