<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetHallResult">
		<GetHallResult>
			<ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<halls>
				<xsl:for-each select="Halls/Hall">
					<hall>
						<code><xsl:value-of select="HallNo" /></code>
						<name><xsl:value-of select="HallName" /></name>
						<seatCount>0</seatCount>
					</hall>
				</xsl:for-each>
			</halls>
		</GetHallResult>
	</xsl:template>
</xsl:stylesheet>