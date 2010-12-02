h1. ${title}

This report was generated on ${date}.
<#list releases.getReleaseNames() as release>

h1. Release ${release}
</#list>

h1. Warnings
