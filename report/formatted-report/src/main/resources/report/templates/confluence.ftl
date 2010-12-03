h1. ${title}

This report was generated on ${date}.
<#list releases.getUsedReleaseNames() as releaseName>
h1. Release ${releaseName}

<#assign heatMaps = releases.getHeatMaps(releaseName)>
<#list heatMaps.getHeatMapNames() as heatMapName>
h4. ${heatMapName}

|| Name || Weight || Issue Count || Change Count || Issues || Formula ||
<#assign heatMap = heatMaps.getHeatMap(heatMapName)>
<#list heatMap.getSortedHeatMapItems() as heatMapItem>
<#assign name = heatMapItem.getHeatMapItemName()>
<#assign weight = heatMapItem.getWeight()>
<#assign issueCount = heatMapItem.getIssuesCount()>
<#assign changeCount = heatMapItem.getChangeCount()>
<#assign issuesList = "">
<#list heatMapItem.getIssues() as issue>
<#assign issuesList = issuesList + issue.getKey() + " ">
</#list>
<#assign formula = heatMapItem.getWeightFormula()>
| {{${name}}} | ${weight} | ${issueCount} | ${changeCount} | ${issuesList} | ${formula} |
</#list>

</#list>
</#list>
h1. Warnings

Not done yet!