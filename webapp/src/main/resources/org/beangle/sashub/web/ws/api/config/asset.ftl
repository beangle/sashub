[#ftl attributes={'content_type':'application/xml'}]
<asset>
  <repository remote="${profile.remoteRepo}" local="${profile.localRepo}"/>
  <contexts>
    [#list assets as asset]
    <context base="${asset.base}">
      [#list asset.bundles as bundle]
      [#if bundle.gav??]
      <jar gav="${bundle.gav}"/>
      [#else]
      <dir location="${bundle.location}"/>
      [/#if]
      [/#list]
    </context>
    [/#list]
  </contexts>
</asset>
