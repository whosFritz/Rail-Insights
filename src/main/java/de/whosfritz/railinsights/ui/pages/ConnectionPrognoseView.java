package de.whosfritz.railinsights.ui.pages;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.whosfritz.railinsights.ui.layout.MainView;
import org.vaadin.addons.simpletimeline.SimpleTimeline;
import org.vaadin.addons.simpletimeline.SimpleTimelineItem;

@Route(value = "verbindungsprognose", layout = MainView.class)
public class ConnectionPrognoseView extends VerticalLayout {
    public ConnectionPrognoseView() {
        SimpleTimeline t = new SimpleTimeline("Release schedule");
        t.add(0, "München Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(10, "Stuttgart Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(20, "Köln Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(30, "Köthen Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(40, "Berlin Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);

        t.add(60, "Hannover Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(70, "Hamburg Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(80, "Kiel Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(90, "Flensburg Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.add(100, "Westerland Hbf").addClassNames(SimpleTimelineItem.STYLE_PRIMARY, SimpleTimelineItem.VARIANT_FILLED);
        t.setBreaks(50);

        t.setWidth("100%");
        add(t);
    }
}
