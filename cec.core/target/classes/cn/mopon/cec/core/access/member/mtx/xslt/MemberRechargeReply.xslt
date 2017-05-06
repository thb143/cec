<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/CardRechargeReturn">
		<CardRecharge>	
			<code><xsl:value-of select="ResultCode"/></code>
		    <card>
		    	<balance>
			       		<xsl:choose>
								<xsl:when test="Balance = ''">0</xsl:when>
								<xsl:otherwise><xsl:value-of select="Balance"/></xsl:otherwise>
						</xsl:choose>
			    </balance>
		    </card>
		</CardRecharge>
	</xsl:template>
</xsl:stylesheet>