package com.timestored.sqldash.forms;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.timestored.sqldash.model.DesktopModel;
import com.timestored.sqldash.model.Widget;
import com.timestored.sqldash.model.WidgetDTO;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("form")
public class FormWidgetDTO
        implements WidgetDTO {
    @XStreamAsAttribute
    int layout;
    @XStreamAsAttribute
    int id;
    @XStreamAsAttribute
    String title;
    List<WidgetDTO> widgets;

    FormWidgetDTO(FormWidget formWidget) {
        this.id = formWidget.getId();
        this.title = formWidget.getTitle();
        this.layout = formWidget.getLayout();
        this.widgets = new ArrayList<>();
        for (Widget w : formWidget.ws)
            this.widgets.add(w.getDTO());
    }

    public Widget newInstance(DesktopModel desktopModel) {
        return new FormWidget(desktopModel, this);
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\forms\FormWidgetDTO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */