package com.company.truonghoc.web.screens.chitietthu;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.truonghoc.entity.Chitietthu;

import javax.inject.Inject;

@UiController("truonghoc_Chitietthu.edit")
@UiDescriptor("chitietthu-edit.xml")
@EditedEntityContainer("chitietthuDc")
@LoadDataBeforeShow
public class ChitietthuEdit extends StandardEditor<Chitietthu> {
    @Inject
    protected TextField<Long> tonggiaField;
    @Inject
    protected TextField<Long> soluongField;
    @Inject
    protected TextField<Long> dongiaField;

    @Subscribe
    protected void onInit(InitEvent event) {
        tonggiaField.setEditable(false);
    }

    @Subscribe("soluongField")
    protected void onSoluongFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtong();
    }

    @Subscribe("dongiaField")
    protected void onDongiaFieldValueChange(HasValue.ValueChangeEvent<Long> event) {
        tinhtong();
    }

    private void tinhtong() {
        if (soluongField.getValue() == null) {
            tonggiaField.setValue(dongiaField.getValue());
        } else {
            if (dongiaField.getValue() == null){
                tonggiaField.clear();
            }else {
                tonggiaField.setValue(soluongField.getValue() * dongiaField.getValue());

            }
        }
    }
}