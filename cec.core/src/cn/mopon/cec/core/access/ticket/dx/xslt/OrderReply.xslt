<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/root/data">
		<ticketOrder>
			<status>
				<xsl:choose>
					<xsl:when test="status= 'success'">4</xsl:when>
					<xsl:otherwise>5</xsl:otherwise>
				</xsl:choose>
			</status>
			<voucher>	
				<printCode><xsl:value-of select="ticketFlag1" /></printCode>
				<verifyCode><xsl:value-of select="ticketFlag2" /></verifyCode>
			</voucher>
		</ticketOrder>	
	</xsl:template>
</xsl:stylesheet>