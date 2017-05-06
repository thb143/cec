<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetDiscountReturn">
	  <CardDiscount>	
	      <code><xsl:value-of select="ResultCode"/></code>
	      <msg><xsl:value-of select="ResultMsg"/></msg>
	      <discountType><xsl:value-of select="DiscountType"/></discountType>
	      <card>
      		  <discountPrice>
		       		<xsl:choose>
							<xsl:when test="Price = ''">0</xsl:when>
							<xsl:otherwise><xsl:value-of select="Price"/></xsl:otherwise>
					</xsl:choose>
		      </discountPrice>
	      </card>
	  </CardDiscount>
	</xsl:template>
</xsl:stylesheet>