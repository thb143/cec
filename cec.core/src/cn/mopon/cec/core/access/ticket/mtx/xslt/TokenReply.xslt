<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/TokenResult">
		 <TokenResult>
				<Token>
					<resultCode><xsl:value-of select="ResultCode" /></resultCode>
					<tokenId><xsl:value-of select="TokenID" /></tokenId>
					<token><xsl:value-of select="Token" /></token>
					<timeOut><xsl:value-of select="TimeOut" /></timeOut>
				</Token>
			</TokenResult>
	</xsl:template>
</xsl:stylesheet>