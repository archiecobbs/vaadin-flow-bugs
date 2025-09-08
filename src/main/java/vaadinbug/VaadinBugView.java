
/*
 * Copyright (C) 2022 Archie L. Cobbs. All rights reserved.
 */

package vaadinbug;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PreserveOnRefresh
@Route("")
@SuppressWarnings("serial")
public class VaadinBugView extends VerticalLayout {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public VaadinBugView() {

        // Add title
        this.add(new Text("Vaadin Bug Demo"));

        // Build tree data provider
        final TreeData<String> treeData = new TreeData<>();
        treeData.addRootItems("AAA", "BBB", "CCC");
        treeData.addItems("AAA", "1a", "2a", "3a");
        treeData.addItems("BBB", "1b", "2b", "3b");
        treeData.addItems("CCC", "1c", "2c", "3c");
        final TreeDataProvider<String> treeDataProvider = new TreeDataProvider<>(treeData);

        // Build TreeGrid
        final TreeGrid<String> treeGrid = new TreeGrid<>();
        treeGrid.addComponentHierarchyColumn(string -> new Html("<bold>" + string + "</bold>"))
          .setResizable(true)
          .setAutoWidth(true);
        treeGrid.setSelectionMode(Grid.SelectionMode.NONE);
        treeGrid.setDataProvider(treeDataProvider);

        // THIS LINE CAUSES THE GRID TO NOT APPEAR
        treeGrid.setSizeFull();

        // THIS LINE CAUSES THE GRID TO REAPPEAR
        this.setSizeFull();

        // Add TreeGrid
        this.add(treeGrid);
    }
}
